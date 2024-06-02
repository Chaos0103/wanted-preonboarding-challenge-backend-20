package com.wanted.challenge.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {

    SELLING("판매중"),
    RESERVATION("예약중"),
    COMPLETION("완료");

    private final String text;
}
