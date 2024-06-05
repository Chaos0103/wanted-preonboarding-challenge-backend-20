package com.wanted.challenge.domain.product.repository.response;

import com.wanted.challenge.domain.product.Product;
import com.wanted.challenge.domain.product.ProductStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final String productStatus;

    @Builder
    public ProductResponse(Long productId, String productName, int productPrice, ProductStatus productStatus) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStatus = productStatus.getText();
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
            .productId(product.getId())
            .productName(product.getProductName())
            .productPrice(product.getPrice())
            .productStatus(product.getStatus())
            .build();
    }
}
