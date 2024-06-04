package com.wanted.challenge.api.service.product;

import com.wanted.challenge.api.service.product.request.ProductCreateServiceRequest;
import com.wanted.challenge.api.service.product.response.ProductCreateResponse;
import com.wanted.challenge.domain.member.Member;
import com.wanted.challenge.domain.member.repository.MemberRepository;
import com.wanted.challenge.domain.order.Order;
import com.wanted.challenge.domain.order.repository.OrderRepository;
import com.wanted.challenge.domain.product.Product;
import com.wanted.challenge.domain.product.repository.ProductRepository;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.NoSuchElementException;

import static com.wanted.challenge.common.message.ExceptionMessage.*;

@RequiredArgsConstructor
@Service
@Transactional
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public ProductCreateResponse createProduct(final String memberKey, @Valid final ProductCreateServiceRequest request) {
        final Member member = findMember(memberKey);

        final Product product = Product.create(request.getProductName(), request.getProductPrice(), member);
        final Product savedProduct = productRepository.save(product);

        return ProductCreateResponse.of(savedProduct);
    }

    public ProductResponse startTransaction(final String memberKey, final long productId) {
        final Member member = findMember(memberKey);
        final Product product = findProduct(productId);

        product.startTransaction();

        final Order order = Order.create(member, product);
        orderRepository.save(order);

        return ProductResponse.of(product);
    }

    public ProductResponse saleApproval(final String ownerMemberKey, final long productId, final long orderId) {
        //회원+상품 조회
        Product product = productRepository.findWithMemberById(productId)
            .orElseThrow(() -> new NoSuchElementException(NO_SUCH_PRODUCT));

        //상품 판매자가 맞는지 검증
        if (!product.isOwner(ownerMemberKey)) {
            throw new IllegalArgumentException(NO_AUTH);
        }

        //주문 조회
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NoSuchElementException(NO_SUCH_ORDER));

        //주문 상태 변경 + 상품 상태 변경
        order.saleApproval();

        return ProductResponse.of(product);
    }

    private Member findMember(final String memberKey) {
        return memberRepository.findByMemberKey(memberKey)
            .orElseThrow(() -> new NoSuchElementException(NO_SUCH_MEMBER));
    }

    private Product findProduct(final long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException(NO_SUCH_PRODUCT));
    }
}
