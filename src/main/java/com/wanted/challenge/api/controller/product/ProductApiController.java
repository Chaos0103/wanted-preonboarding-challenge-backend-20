package com.wanted.challenge.api.controller.product;

import com.wanted.challenge.api.ApiResponse;
import com.wanted.challenge.api.PageResponse;
import com.wanted.challenge.api.controller.product.request.ProductCreateRequest;
import com.wanted.challenge.api.controller.product.request.ProductSearchParam;
import com.wanted.challenge.api.service.product.ProductQueryService;
import com.wanted.challenge.api.service.product.ProductService;
import com.wanted.challenge.api.service.product.response.ProductCreateResponse;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductApiController {

    private final ProductService productService;
    private final ProductQueryService productQueryService;

    /**
     * 제품 신규 등록 API
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductCreateResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        ProductCreateResponse response = productService.createProduct("memberKey", request.toServiceRequest());

        return ApiResponse.created(response);
    }

    /**
     * 제품 목록 조회 API
     */
    @GetMapping
    public ApiResponse<PageResponse<ProductResponse>> searchProducts(@Valid @ModelAttribute ProductSearchParam param) {
        PageResponse<ProductResponse> response = productQueryService.searchProducts(param.getPage(), param.getKeyword());

        return ApiResponse.ok(response);
    }

    /**
     * 제품 상세 조회 API
     */
    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> searchProduct(@PathVariable Long productId) {
        ProductResponse response = productQueryService.searchProduct(productId);

        return ApiResponse.ok(response);
    }

    /**
     * 제품 거래 시작 API
     */
    @PostMapping("/{productId}/start-transaction")
    public ApiResponse<ProductResponse> startTransaction(@PathVariable Long productId) {
        ProductResponse response = productService.startTransaction("memberKey", productId);

        return ApiResponse.ok(response);
    }

    /**
     * 제품 판매 승인 API
     */
    @PostMapping("/{productId}/sale-approval")
    public ApiResponse<ProductResponse> saleApproval(@PathVariable Long productId) {
        ProductResponse response = productService.saleApproval("memberKey", productId);

        return ApiResponse.ok(response);
    }
}
