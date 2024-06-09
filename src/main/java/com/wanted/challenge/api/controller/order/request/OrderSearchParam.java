package com.wanted.challenge.api.controller.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class OrderSearchParam {

    private int page;

    @Builder
    private OrderSearchParam(int page) {
        this.page = page;
    }
}
