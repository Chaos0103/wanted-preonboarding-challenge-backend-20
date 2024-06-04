package com.wanted.challenge.api.service.product;

import com.wanted.challenge.IntegrationTestSupport;
import com.wanted.challenge.api.service.product.request.ProductCreateServiceRequest;
import com.wanted.challenge.api.service.product.response.ProductCreateResponse;
import com.wanted.challenge.domain.member.Member;
import com.wanted.challenge.domain.member.repository.MemberRepository;
import com.wanted.challenge.domain.order.Order;
import com.wanted.challenge.domain.order.OrderStatus;
import com.wanted.challenge.domain.order.repository.OrderRepository;
import com.wanted.challenge.domain.product.Product;
import com.wanted.challenge.domain.product.ProductStatus;
import com.wanted.challenge.domain.product.repository.ProductRepository;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.wanted.challenge.common.message.ExceptionMessage.*;
import static com.wanted.challenge.common.message.ValidationMessage.OUT_OF_LENGTH_PRODUCT_NAME;
import static org.assertj.core.api.Assertions.*;

class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("입력 받은 제품명의 길이가 100자 초과하면 예외가 발생한다.")
    @Test
    void createProductWithOutOfMaxLengthByProductName() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey, "wanted@gmail.com");

        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .productName("a".repeat(101))
            .productPrice(1_200_000)
            .build();

        //when
        assertThatThrownBy(() -> productService.createProduct(memberKey, request))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessage("createProduct.request.productName: " + OUT_OF_LENGTH_PRODUCT_NAME);

        //then
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(0);
    }

    @DisplayName("입력 받은 회원 고유키가 일치하는 회원이 존재하지 않으면 예외가 발생한다.")
    @Test
    void createProductWithNotMatchMemberKey() {
        //given
        String memberKey = UUID.randomUUID().toString();

        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .build();

        //when
        assertThatThrownBy(() -> productService.createProduct(memberKey, request))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(NO_SUCH_MEMBER);

        //then
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(0);
    }

    @DisplayName("제품 정보를 입력 받아 제품 신규 등록을 한다.")
    @Test
    void createProduct() {
        //given
        String memberKey = UUID.randomUUID().toString();
        Member member = createMember(memberKey, "wanted@gmail.com");

        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .productPrice(1_200_000)
            .build();

        //when
        ProductCreateResponse response = productService.createProduct(memberKey, request);

        //then
        assertThat(response)
            .isNotNull()
            .hasFieldOrPropertyWithValue("productName", "MacBook Air 15 M2 256GB 스페이스그레이")
            .hasFieldOrPropertyWithValue("productPrice", 1_200_000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
            .extracting("productName", "price", "status")
            .containsExactlyInAnyOrder(
                tuple("MacBook Air 15 M2 256GB 스페이스그레이", 1_200_000, ProductStatus.SELLING)
            );
    }

    @DisplayName("입력 받은 구매자 회원 고유키가 일치하는 회원이 존재하지 않으면 예외가 발생한다.")
    @Test
    void startTransactionWithoutMember() {
        //given
        String ownerMemberKey = UUID.randomUUID().toString();
        Member owner = createMember(ownerMemberKey, "owner@gmail.com");
        Product product = createProduct(owner, ProductStatus.SELLING);

        String consumerMemberKey = UUID.randomUUID().toString();

        //when
        assertThatThrownBy(() -> productService.startTransaction(consumerMemberKey, product.getId()))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(NO_SUCH_MEMBER);

        //then
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
            .extracting("id", "status")
            .containsExactlyInAnyOrder(
                tuple(product.getId(), ProductStatus.SELLING)
            );

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(0);
    }

    @DisplayName("입력 받은 제품 식별키가 일치하는 제품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void startTransactionWithoutProduct() {
        //given
        String consumerMemberKey = UUID.randomUUID().toString();
        Member consumer = createMember(consumerMemberKey, "consumer@gmail.com");

        //when
        assertThatThrownBy(() -> productService.startTransaction(consumerMemberKey, 1L))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(NO_SUCH_PRODUCT);

        //then
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(0);
    }

    @DisplayName("거래를 시작하려는 제품의 상태가 판매중이 아니라면 예외가 발생한다.")
    @Test
    void startTransactionNotSelling() {
        //given
        String ownerMemberKey = UUID.randomUUID().toString();
        Member owner = createMember(ownerMemberKey, "owner@gmail.com");
        Product product = createProduct(owner, ProductStatus.COMPLETION);

        String consumerMemberKey = UUID.randomUUID().toString();
        Member consumer = createMember(consumerMemberKey, "consumer@gmail.com");

        //when
        assertThatThrownBy(() -> productService.startTransaction(consumerMemberKey, product.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NON_PURCHASE_PRODUCT);

        //then
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
            .extracting("id", "status")
            .containsExactlyInAnyOrder(
                tuple(product.getId(), ProductStatus.COMPLETION)
            );

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(0);
    }

    @DisplayName("구매자 회원 고유키와 거래할 제품 식별키를 입력 받아 거래를 시작한다.")
    @Test
    void startTransaction() {
        //given
        String ownerMemberKey = UUID.randomUUID().toString();
        Member owner = createMember(ownerMemberKey, "owner@gmail.com");
        Product product = createProduct(owner, ProductStatus.SELLING);

        String consumerMemberKey = UUID.randomUUID().toString();
        Member consumer = createMember(consumerMemberKey, "consumer@gmail.com");

        //when
        ProductResponse response = productService.startTransaction(consumerMemberKey, product.getId());

        //then
        assertThat(response)
            .isNotNull()
            .hasFieldOrPropertyWithValue("productId", product.getId())
            .hasFieldOrPropertyWithValue("productName", "MacBook Air 15 M2 256GB 스페이스그레이")
            .hasFieldOrPropertyWithValue("productPrice", 1_200_000)
            .hasFieldOrPropertyWithValue("productStatus", ProductStatus.RESERVATION.getText());

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
            .extracting("id", "status")
            .containsExactlyInAnyOrder(
                tuple(product.getId(), ProductStatus.RESERVATION)
            );

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1)
            .extracting("orderStatus")
            .containsExactlyInAnyOrder(
                OrderStatus.ORDER
            );
    }

    @DisplayName("입력 받은 제품 식별키가 일치하는 제품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void saleApprovalWithoutProduct() {
        //given
        String ownerMemberKey = UUID.randomUUID().toString();
        Member owner = createMember(ownerMemberKey, "owner@gmail.com");

        //when
        assertThatThrownBy(() -> productService.saleApproval(ownerMemberKey, 1L, 1L))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(NO_SUCH_PRODUCT);

        //then
    }

    @DisplayName("입력 받은 주문 식별키가 일치하는 주문이 존재하지 않으면 예외가 발생한다.")
    @Test
    void saleApprovalWithoutOrder() {
        //given
        String ownerMemberKey = UUID.randomUUID().toString();
        Member owner = createMember(ownerMemberKey, "owner@gmail.com");
        Product product = createProduct(owner, ProductStatus.RESERVATION);

        String consumerMemberKey = UUID.randomUUID().toString();
        Member consumer = createMember(consumerMemberKey, "consumer@gmail.com");

        //when
        assertThatThrownBy(() -> productService.saleApproval(ownerMemberKey, product.getId(), 1L))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(NO_SUCH_ORDER);

        //then
    }

    @DisplayName("판매승인을 할 때 판매자 본인의 제품이 아니라면 예외가 발생한다.")
    @Test
    void saleApprovalWithoutAuth() {
        //given
        String ownerMemberKey = UUID.randomUUID().toString();
        Member owner = createMember(ownerMemberKey, "owner@gmail.com");
        Product product = createProduct(owner, ProductStatus.RESERVATION);

        String consumerMemberKey = UUID.randomUUID().toString();
        Member consumer = createMember(consumerMemberKey, "consumer@gmail.com");

        Order order = createOrder(consumer, product, OrderStatus.ORDER);

        String anotherMemberKey = UUID.randomUUID().toString();
        Member another = createMember(anotherMemberKey, "another@gmail.com");

        //when
        assertThatThrownBy(() -> productService.saleApproval(anotherMemberKey, product.getId(), order.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NO_AUTH);

        //then
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
            .extracting("id", "status")
            .containsExactlyInAnyOrder(
                tuple(product.getId(), ProductStatus.RESERVATION)
            );

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1)
            .extracting("id", "orderStatus")
            .containsExactlyInAnyOrder(
                tuple(order.getId(), OrderStatus.ORDER)
            );
    }

    @DisplayName("판매자는 거래 진행중인 구매자에 대해 판매승인을 하는 경우 거래가 완료된다.")
    @Test
    void saleApproval() {
        //given
        String ownerMemberKey = UUID.randomUUID().toString();
        Member owner = createMember(ownerMemberKey, "owner@gmail.com");
        Product product = createProduct(owner, ProductStatus.RESERVATION);

        String consumerMemberKey = UUID.randomUUID().toString();
        Member consumer = createMember(consumerMemberKey, "consumer@gmail.com");

        Order order = createOrder(consumer, product, OrderStatus.ORDER);

        //when
        ProductResponse response = productService.saleApproval(ownerMemberKey, product.getId(), order.getId());

        //then
        assertThat(response)
            .isNotNull()
            .hasFieldOrPropertyWithValue("productId", product.getId())
            .hasFieldOrPropertyWithValue("productName", "MacBook Air 15 M2 256GB 스페이스그레이")
            .hasFieldOrPropertyWithValue("productPrice", 1_200_000)
            .hasFieldOrPropertyWithValue("productStatus", ProductStatus.COMPLETION.getText());

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
            .extracting("id", "status")
            .containsExactlyInAnyOrder(
                tuple(product.getId(), ProductStatus.COMPLETION)
            );

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1)
            .extracting("id", "orderStatus")
            .containsExactlyInAnyOrder(
                tuple(order.getId(), OrderStatus.COMPLETE)
            );
    }

    private Member createMember(String memberKey, String email) {
        Member member = Member.builder()
            .isDeleted(false)
            .memberKey(memberKey)
            .email(email)
            .pwd("wanted1234!")
            .name("원티드")
            .build();
        return memberRepository.save(member);
    }

    private Product createProduct(Member member, ProductStatus status) {
        Product product = Product.builder()
            .isDeleted(false)
            .productName("MacBook Air 15 M2 256GB 스페이스그레이")
            .price(1_200_000)
            .status(status)
            .member(member)
            .build();
        return productRepository.save(product);
    }

    private Order createOrder(Member member, Product product, OrderStatus orderStatus) {
        Order order = Order.builder()
            .member(member)
            .product(product)
            .orderStatus(orderStatus)
            .orderDateTime(LocalDateTime.now())
            .build();
        return orderRepository.save(order);
    }
}