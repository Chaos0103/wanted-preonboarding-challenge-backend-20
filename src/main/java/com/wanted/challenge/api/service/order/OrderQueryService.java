package com.wanted.challenge.api.service.order;

import com.wanted.challenge.api.PageResponse;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderQueryService {

    public PageResponse<ProductResponse> searchOrders(final String memberKey) {
        return null;
    }
}
