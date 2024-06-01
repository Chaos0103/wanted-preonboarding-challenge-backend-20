package com.wanted.challenge.api.service.product.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCreateServiceRequest {

    private final String productName;
    private final int productPrice;

    @Builder
    private ProductCreateServiceRequest(String productName, int productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }
}
