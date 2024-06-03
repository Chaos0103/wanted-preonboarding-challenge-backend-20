package com.wanted.challenge.domain.product.repository.response;

import com.wanted.challenge.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final String productStatus;

    @Builder
    private ProductResponse(Long productId, String productName, int productPrice, String productStatus) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
            .productId(product.getId())
            .productName(product.getProductName())
            .productPrice(product.getPrice())
            .productStatus(product.getStatus().getText())
            .build();
    }
}
