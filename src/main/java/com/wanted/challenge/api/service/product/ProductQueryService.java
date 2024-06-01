package com.wanted.challenge.api.service.product;

import com.wanted.challenge.api.PageResponse;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductQueryService {

    public PageResponse<ProductResponse> searchProducts(final int page, final String keyword) {
        return null;
    }

    public ProductResponse searchProduct(final long productId) {
        return null;
    }
}
