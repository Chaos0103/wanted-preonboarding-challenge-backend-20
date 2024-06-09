package com.wanted.challenge.api.service.order;

import com.wanted.challenge.api.PageResponse;
import com.wanted.challenge.common.utils.PageUtils;
import com.wanted.challenge.domain.order.repository.OrderQueryRepository;
import com.wanted.challenge.domain.order.repository.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;

    public PageResponse<OrderResponse> searchOrders(final String memberKey, final int page) {
        final Pageable pageable = PageUtils.generate(page);

        final List<Long> orderIds = orderQueryRepository.findAllOrderIdByCond(memberKey, pageable);
        final long totalCount = orderQueryRepository.countByCond(memberKey);

        if (CollectionUtils.isEmpty(orderIds)) {
            return PageResponse.of(new PageImpl<>(new ArrayList<>(), pageable, totalCount));
        }

        final List<OrderResponse> content = orderQueryRepository.findAllByIdIn(orderIds);

        return PageResponse.of(new PageImpl<>(content, pageable, totalCount));
    }
}
