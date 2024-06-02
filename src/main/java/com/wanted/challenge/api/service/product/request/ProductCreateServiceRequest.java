package com.wanted.challenge.api.service.product.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import static com.wanted.challenge.common.message.ValidationMessage.OUT_OF_LENGTH_PRODUCT_NAME;

@Getter
public class ProductCreateServiceRequest {

    @Size(max = 100, message = OUT_OF_LENGTH_PRODUCT_NAME)
    private final String productName;

    private final int productPrice;

    @Builder
    private ProductCreateServiceRequest(String productName, int productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }
}
