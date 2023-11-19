package com.imeasystems.orderservice.order.repository;

import com.imeasystems.orderservice.order.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    List<OrderHistory> findAllByOrderId(Long orderId);
}
