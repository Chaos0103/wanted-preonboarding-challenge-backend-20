package com.wanted.challenge.api.service.product.response;

import com.wanted.challenge.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductCreateResponse {

    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final LocalDateTime createdDateTime;

    @Builder
    private ProductCreateResponse(Long productId, String productName, int productPrice, LocalDateTime createdDateTime) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.createdDateTime = createdDateTime;
    }

    public static ProductCreateResponse of(Product product) {
        return ProductCreateResponse.builder()
            .productId(product.getId())
            .productName(product.getProductName())
            .productPrice(product.getPrice())
            .createdDateTime(product.getCreatedDateTime())
            .build();
    }
}
