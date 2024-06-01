package com.wanted.challenge.api.service.product;

import com.wanted.challenge.api.service.product.request.ProductCreateServiceRequest;
import com.wanted.challenge.api.service.product.response.ProductCreateResponse;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {

    public ProductCreateResponse createProduct(final String memberKey, final ProductCreateServiceRequest request) {
        return null;
    }

    public ProductResponse startTransaction(final String memberKey, final long productId) {
        return null;
    }

    public ProductResponse saleApproval(final String memberKey, final long productId) {
        return null;
    }
}
