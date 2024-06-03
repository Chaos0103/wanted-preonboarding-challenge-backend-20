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
@IdClass(OrderId.class)
public class Order extends TimeBaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDateTime;

    @Builder
    private Order(boolean isDeleted, Member member, Product product, LocalDateTime orderDateTime) {
        super(isDeleted);
        this.member = member;
        this.product = product;
        this.orderDateTime = orderDateTime;
    }

    public static Order create(Member member, Product product) {
        return Order.builder()
            .isDeleted(false)
            .member(member)
            .product(product)
            .orderDateTime(LocalDateTime.now())
            .build();
    }
}
