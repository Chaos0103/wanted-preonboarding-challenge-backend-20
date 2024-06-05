package com.wanted.challenge.domain.product.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.challenge.domain.product.repository.response.ProductResponse;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.wanted.challenge.domain.product.QProduct.*;
import static org.springframework.util.StringUtils.*;

@Repository
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ProductQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Long> findProductIdByCond(final String keyword, final Pageable pageable) {
        return queryFactory
            .select(product.id)
            .from(product)
            .where(
                product.isDeleted.isFalse(),
                likeKeyword(keyword)
            )
            .orderBy(
                product.createdDateTime.desc()
            )
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();
    }

    public List<ProductResponse> findByIdIn(final List<Long> productIds) {
        return queryFactory
            .select(
                Projections.constructor(
                    ProductResponse.class,
                    product.id.as("productId"),
                    product.productName,
                    product.price.as("productPrice"),
                    product.status.as("productStatus")
                )
            )
            .from(product)
            .where(product.id.in(productIds))
            .orderBy(product.createdDateTime.desc())
            .fetch();
    }

    public long countByCond(final String keyword) {
        return queryFactory
            .select(
                product.id
            )
            .from(product)
            .where(
                product.isDeleted.isFalse(),
                likeKeyword(keyword)
            )
            .fetch()
            .size();
    }

    public Optional<ProductResponse> findById(final long productId) {
        ProductResponse content = queryFactory
            .select(
                Projections.constructor(
                    ProductResponse.class,
                    Expressions.asNumber(productId),
                    product.productName,
                    product.price.as("productPrice"),
                    product.status.as("productStatus")
                )
            )
            .from(product)
            .where(
                product.isDeleted.isFalse(),
                product.id.eq(productId)
            )
            .fetchFirst();
        return Optional.ofNullable(content);
    }

    private BooleanExpression likeKeyword(final String keyword) {
        return hasText(keyword) ? product.productName.like("%" + keyword + "%") : null;
    }
}
