package com.wanted.challenge.api.controller.product.request;

import com.wanted.challenge.api.service.product.request.ProductCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.wanted.challenge.common.message.BindingMessage.NOT_BLANK_PRODUCT_NAME;
import static com.wanted.challenge.common.message.BindingMessage.POSITIVE_PRODUCT_NAME;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = NOT_BLANK_PRODUCT_NAME)
    private String productName;

    @Positive(message = POSITIVE_PRODUCT_NAME)
    private Integer productPrice;

    @Builder
    private ProductCreateRequest(String productName, Integer productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public ProductCreateServiceRequest toServiceRequest() {
        return ProductCreateServiceRequest.builder()
            .productName(productName.strip())
            .productPrice(productPrice)
            .build();
    }
}
