package com.wanted.challenge.api.controller.order;

import com.wanted.challenge.api.ApiResponse;
import com.wanted.challenge.api.PageResponse;
import com.wanted.challenge.api.service.order.OrderQueryService;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderApiController {

    private final OrderQueryService orderQueryService;

    @GetMapping
    public ApiResponse<PageResponse<ProductResponse>> searchOrders() {
        PageResponse<ProductResponse> response = orderQueryService.searchOrders("memberKey");

        return ApiResponse.ok(response);
    }
}
