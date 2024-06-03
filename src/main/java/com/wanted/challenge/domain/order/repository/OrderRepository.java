package com.wanted.challenge.domain.order.repository;

import com.wanted.challenge.domain.order.Order;
import com.wanted.challenge.domain.order.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, OrderId> {
}
