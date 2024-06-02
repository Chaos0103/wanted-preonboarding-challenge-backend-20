package com.wanted.challenge.api.service.product;

import com.wanted.challenge.api.service.product.request.ProductCreateServiceRequest;
import com.wanted.challenge.api.service.product.response.ProductCreateResponse;
import com.wanted.challenge.domain.member.Member;
import com.wanted.challenge.domain.member.repository.MemberRepository;
import com.wanted.challenge.domain.product.Product;
import com.wanted.challenge.domain.product.repository.ProductRepository;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.NoSuchElementException;

import static com.wanted.challenge.common.message.ExceptionMessage.NO_SUCH_MEMBER;

@RequiredArgsConstructor
@Service
@Transactional
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public ProductCreateResponse createProduct(final String memberKey, @Valid final ProductCreateServiceRequest request) {
        final Member member = memberRepository.findByMemberKey(memberKey)
            .orElseThrow(() -> new NoSuchElementException(NO_SUCH_MEMBER));

        final Product product = Product.create(request.getProductName(), request.getProductPrice(), member);
        final Product savedProduct = productRepository.save(product);

        return ProductCreateResponse.of(savedProduct);
    }

    public ProductResponse startTransaction(final String memberKey, final long productId) {
        return null;
    }

    public ProductResponse saleApproval(final String memberKey, final long productId) {
        return null;
    }
}
