package com.imeasystems.orderservice.order.service;

import com.imeasystems.orderservice.order.dto.CreateOrderDto;
import com.imeasystems.orderservice.order.dto.OrderDto;
import com.imeasystems.orderservice.order.dto.OrderResponse;
import com.imeasystems.orderservice.order.dto.UpdateOrderDto;
import org.springframework.data.domain.PageRequest;

public interface OrderService {

    OrderDto createOrder(CreateOrderDto createOrderDto);

    OrderDto getOrder(Long id);

    OrderResponse getAllOrders(PageRequest pageRequest);

    void updateOrder(Long id, UpdateOrderDto updateOrderDto);

    void deleteOrder(Long id);
}
