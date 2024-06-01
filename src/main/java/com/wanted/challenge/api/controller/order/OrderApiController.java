package com.wanted.challenge.api.controller.order;

import com.wanted.challenge.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderApiController {

    @GetMapping
    public ApiResponse<?> searchOrders() {
        return ApiResponse.ok(null);
    }
}
