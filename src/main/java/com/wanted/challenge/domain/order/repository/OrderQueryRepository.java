package com.wanted.challenge.domain.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.challenge.domain.order.repository.response.OrderResponse;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.wanted.challenge.domain.member.QMember.member;
import static com.wanted.challenge.domain.order.QOrder.order;
import static com.wanted.challenge.domain.product.QProduct.product;

@Repository
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public OrderQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Long> findAllOrderIdByCond(final String memberKey, final Pageable pageable) {
        return queryFactory
            .select(order.id)
            .from(order)
            .join(order.member, member)
            .where(
                order.isDeleted.isFalse(),
                member.memberKey.eq(memberKey)
            )
            .orderBy(order.orderDateTime.desc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();
    }

    public List<OrderResponse> findAllByIdIn(final List<Long> orderIds) {
        return queryFactory
            .select(
                Projections.constructor(
                    OrderResponse.class,
                    product.id.as("productId"),
                    product.productName,
                    product.price.as("productPrice"),
                    order.orderStatus,
                    order.orderDateTime
                )
            )
            .from(order)
            .join(order.product, product)
            .where(order.id.in(orderIds))
            .orderBy(order.orderDateTime.desc())
            .fetch();
    }

    public long countByCond(final String memberKey) {
        return queryFactory
            .select(order.id)
            .from(order)
            .join(order.member, member)
            .where(
                order.isDeleted.isFalse(),
                member.memberKey.eq(memberKey)
            )
            .fetch()
            .size();
    }
}
