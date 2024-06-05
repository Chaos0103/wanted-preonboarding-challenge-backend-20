package com.wanted.challenge.api.service.product;

import com.wanted.challenge.api.PageResponse;
import com.wanted.challenge.common.utils.PageUtils;
import com.wanted.challenge.domain.product.repository.ProductQueryRepository;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.wanted.challenge.common.message.ExceptionMessage.NO_SUCH_PRODUCT;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;

    public PageResponse<ProductResponse> searchProducts(final int page, final String keyword) {
        Pageable pageable = PageUtils.generate(page);

        List<Long> productIds = productQueryRepository.findProductIdByCond(keyword, pageable);
        long totalCount = productQueryRepository.countByCond(keyword);

        if (CollectionUtils.isEmpty(productIds)) {
            return PageResponse.of(new PageImpl<>(new ArrayList<>(), pageable, totalCount));
        }

        List<ProductResponse> content = productQueryRepository.findByIdIn(productIds);

        return PageResponse.of(new PageImpl<>(content, pageable, totalCount));
    }

    public ProductResponse searchProduct(final long productId) {
        return productQueryRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException(NO_SUCH_PRODUCT));
    }
}
