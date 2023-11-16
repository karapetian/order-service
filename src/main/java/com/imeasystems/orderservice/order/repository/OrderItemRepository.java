package com.imeasystems.orderservice.order.repository;

import com.imeasystems.orderservice.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}