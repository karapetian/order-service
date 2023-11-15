package com.imeasystems.orderservice.order.repository;

import com.imeasystems.orderservice.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
