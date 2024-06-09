package com.wanted.challenge.api.controller.order;

import com.wanted.challenge.api.ApiResponse;
import com.wanted.challenge.api.PageResponse;
import com.wanted.challenge.api.controller.order.request.OrderSearchParam;
import com.wanted.challenge.api.service.order.OrderQueryService;
import com.wanted.challenge.common.utils.SecurityUtils;
import com.wanted.challenge.domain.order.repository.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderApiController {

    private final OrderQueryService orderQueryService;

    @GetMapping
    public ApiResponse<PageResponse<OrderResponse>> searchOrders(@Valid @ModelAttribute OrderSearchParam param) {
        String memberKey = SecurityUtils.getCurrentMemberKey();

        PageResponse<OrderResponse> response = orderQueryService.searchOrders(memberKey, param.getPage());

        return ApiResponse.ok(response);
    }
}
