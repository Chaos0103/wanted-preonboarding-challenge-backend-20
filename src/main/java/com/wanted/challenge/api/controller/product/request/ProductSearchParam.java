package com.wanted.challenge.api.controller.product.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductSearchParam {

    private int page;
    private String keyword;

    @Builder
    private ProductSearchParam(int page, String keyword) {
        this.page = page;
        this.keyword = keyword;
    }
}
