package com.wanted.challenge.api.controller.product.request;

import com.wanted.challenge.api.service.product.request.ProductCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    private String productName;
    private Integer productPrice;

    @Builder
    private ProductCreateRequest(String productName, Integer productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public ProductCreateServiceRequest toServiceRequest() {
        return null;
    }
}
