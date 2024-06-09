package com.wanted.challenge.domain.order.repository.response;

import com.wanted.challenge.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderResponse {

    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final String orderStatus;
    private final LocalDateTime orderDateTime;

    @Builder
    public OrderResponse(Long productId, String productName, int productPrice, OrderStatus orderStatus, LocalDateTime orderDateTime) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.orderStatus = orderStatus.getText();
        this.orderDateTime = orderDateTime;
    }
}
