package com.wanted.challenge.domain.order;

import com.wanted.challenge.domain.TimeBaseEntity;
import com.wanted.challenge.domain.member.Member;
import com.wanted.challenge.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public Order(boolean isDeleted, OrderStatus orderStatus, LocalDateTime orderDateTime, Member member, Product product) {
        super(isDeleted);
        this.orderStatus = orderStatus;
        this.orderDateTime = orderDateTime;
        this.member = member;
        this.product = product;
    }

    public static Order create(Member member, Product product) {
        return Order.builder()
            .isDeleted(false)
            .orderStatus(OrderStatus.ORDER)
            .orderDateTime(LocalDateTime.now())
            .member(member)
            .product(product)
            .build();
    }

    public void saleApproval() {
        orderStatus = OrderStatus.COMPLETE;
        product.saleApproval();
    }
}
