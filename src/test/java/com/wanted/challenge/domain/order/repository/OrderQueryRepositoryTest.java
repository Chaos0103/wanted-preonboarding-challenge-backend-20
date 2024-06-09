package com.wanted.challenge.domain.order.repository;

import com.wanted.challenge.IntegrationTestSupport;
import com.wanted.challenge.domain.member.Member;
import com.wanted.challenge.domain.member.repository.MemberRepository;
import com.wanted.challenge.domain.order.Order;
import com.wanted.challenge.domain.order.OrderStatus;
import com.wanted.challenge.domain.order.repository.response.OrderResponse;
import com.wanted.challenge.domain.product.Product;
import com.wanted.challenge.domain.product.ProductStatus;
import com.wanted.challenge.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class OrderQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private OrderQueryRepository orderQueryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("검색 조건에 맞는 주문 식별키 목록을 조회한다.")
    @Test
    void findAllOrderIdByCond() {
        //given
        String ownerMemberKey = UUID.randomUUID().toString();
        Member owner = createMember(ownerMemberKey, "owner@gmail.com");

        Product product1 = createProduct(owner, "MacBook Air 15 M2 256GB 스페이스그레이", ProductStatus.RESERVATION, false);
        Product product2 = createProduct(owner, "MacBook Air 15 M2 256GB 실버", ProductStatus.COMPLETION, false);

        String consumerMemberKey = UUID.randomUUID().toString();
        Member consumer = createMember(consumerMemberKey, "consumer@gmail.com");

        Order order1 = createOrder(consumer, product1, OrderStatus.ORDER);
        Order order2 = createOrder(consumer, product2, OrderStatus.COMPLETE);

        PageRequest page = PageRequest.of(0, 10);

        //when
        List<Long> orderIds = orderQueryRepository.findAllOrderIdByCond(consumerMemberKey, page);

        //then
        assertThat(orderIds).hasSize(2)
            .contains(order1.getId(), order2.getId());
    }

    @DisplayName("주문 식별키 목록으로 주문 목록을 조회한다.")
    @Test
    void findAllByIdIn() {
        //given
        String ownerMemberKey = UUID.randomUUID().toString();
        Member owner = createMember(ownerMemberKey, "owner@gmail.com");

        Product product1 = createProduct(owner, "MacBook Air 15 M2 256GB 스페이스그레이", ProductStatus.RESERVATION, false);
        Product product2 = createProduct(owner, "MacBook Air 15 M2 256GB 실버", ProductStatus.COMPLETION, false);

        String consumerMemberKey = UUID.randomUUID().toString();
        Member consumer = createMember(consumerMemberKey, "consumer@gmail.com");

        Order order1 = createOrder(consumer, product1, OrderStatus.ORDER);
        Order order2 = createOrder(consumer, product2, OrderStatus.COMPLETE);

        List<Long> orderIds = List.of(order1.getId(), order2.getId());

        //when
        List<OrderResponse> content = orderQueryRepository.findAllByIdIn(orderIds);

        //then
        assertThat(content).hasSize(2)
            .extracting("productId", "productName", "orderStatus")
            .containsExactlyInAnyOrder(
                tuple(product1.getId(), "MacBook Air 15 M2 256GB 스페이스그레이", OrderStatus.ORDER.getText()),
                tuple(product2.getId(), "MacBook Air 15 M2 256GB 실버", OrderStatus.COMPLETE.getText())
            );
    }

    @DisplayName("회원 고유키로 주문 갯수를 조회한다.")
    @Test
    void countByCond() {
        //given
        String ownerMemberKey = UUID.randomUUID().toString();
        Member owner = createMember(ownerMemberKey, "owner@gmail.com");

        Product product1 = createProduct(owner, "MacBook Air 15 M2 256GB 스페이스그레이", ProductStatus.RESERVATION, false);
        Product product2 = createProduct(owner, "MacBook Air 15 M2 256GB 실버", ProductStatus.COMPLETION, false);

        String consumerMemberKey = UUID.randomUUID().toString();
        Member consumer = createMember(consumerMemberKey, "consumer@gmail.com");

        Order order1 = createOrder(consumer, product1, OrderStatus.ORDER);
        Order order2 = createOrder(consumer, product2, OrderStatus.COMPLETE);

        //when
        long totalCount = orderQueryRepository.countByCond(consumerMemberKey);

        //then
        assertThat(totalCount).isEqualTo(2);
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

    private Product createProduct(Member member, String productName, ProductStatus status, boolean isDeleted) {
        Product product = Product.builder()
            .isDeleted(isDeleted)
            .productName(productName)
            .price(10_000)
            .status(status)
            .member(member)
            .build();
        return productRepository.save(product);
    }

    private Order createOrder(Member member, Product product, OrderStatus orderStatus) {
        Order order = Order.builder()
            .isDeleted(false)
            .orderStatus(orderStatus)
            .orderDateTime(LocalDateTime.now())
            .member(member)
            .product(product)
            .build();
        return orderRepository.save(order);
    }
}