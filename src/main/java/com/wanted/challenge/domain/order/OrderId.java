package com.wanted.challenge.domain.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderId implements Serializable {

    private Long member;
    private Long product;

    @Builder
    private OrderId(Long member, Long product) {
        this.member = member;
        this.product = product;
    }
}
