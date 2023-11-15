package com.imeasystems.orderservice.service;

import com.imeasystems.orderservice.dto.CreateOrderDto;
import com.imeasystems.orderservice.dto.OrderDto;
import com.imeasystems.orderservice.dto.OrderResponse;
import com.imeasystems.orderservice.dto.UpdateOrderDto;
import org.springframework.data.domain.PageRequest;

public interface OrderService {

    OrderDto createOrder(CreateOrderDto createOrderDto);

    OrderDto getOrder(Long id);

    OrderResponse getAllOrders(PageRequest pageRequest);

    void updateOrder(Long id, UpdateOrderDto updateOrderDto);

    void deleteOrder(Long id);
}
