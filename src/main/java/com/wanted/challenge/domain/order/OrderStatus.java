package com.wanted.challenge.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    ORDER("주문"),
    COMPLETE("완료");

    private final String text;
}
