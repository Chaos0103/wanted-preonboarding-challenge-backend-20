package com.wanted.challenge.domain.product;

import com.wanted.challenge.domain.TimeBaseEntity;
import com.wanted.challenge.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(11) default 'SELLING'", length = 11)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Product(boolean isDeleted, String productName, int price, ProductStatus status, Member member) {
        super(isDeleted);
        this.productName = productName;
        this.price = price;
        this.status = status;
        this.member = member;
    }

    public static Product create(String productName, int price, Member member) {
        return Product.builder()
            .isDeleted(false)
            .productName(productName)
            .price(price)
            .status(ProductStatus.SELLING)
            .member(member)
            .build();
    }
}
