package com.imeasystems.orderservice.service.impl;

import com.imeasystems.orderservice.dto.CreateOrderDto;
import com.imeasystems.orderservice.dto.OrderDto;
import com.imeasystems.orderservice.dto.OrderResponse;
import com.imeasystems.orderservice.dto.UpdateOrderDto;
import com.imeasystems.orderservice.entity.Order;
import com.imeasystems.orderservice.mapper.OrderMapper;
import com.imeasystems.orderservice.repository.OrderRepository;
import com.imeasystems.orderservice.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    
    private final OrderMapper orderMapper;
    
    @Transactional
    @Override
    public OrderDto createOrder(CreateOrderDto createOrderDto) {
        final Order order = orderMapper.createOrderDtoToOrder(createOrderDto);
        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto getOrder(Long id) {
        return orderMapper.orderToOrderDto(findOrderById(id));
    }

    @Override
    public OrderResponse getAllOrders(PageRequest pageRequest) {
        Page<Order> orderPage = orderRepository.findAll(pageRequest);
        final List<OrderDto> orderDtos = orderMapper.orderListToOrderDtoList(orderPage.getContent());

        return OrderResponse.builder()
                .totalItems(orderPage.getTotalElements())
                .orders(orderDtos)
                .totalPages(orderPage.getTotalPages())
                .currentPage(orderPage.getNumber())
                .build();
    }

    @Transactional
    @Override
    public void updateOrder(Long id, UpdateOrderDto updateOrderDto) {
        Order order = findOrderById(id);

        if (Objects.nonNull(updateOrderDto.getCustomerId())) {
            // getCustomer
        }
        if (Objects.nonNull(updateOrderDto.getStatus())) {
            order.setStatus(updateOrderDto.getStatus());
        }
        if (Objects.nonNull(updateOrderDto.getShippingAddress())) {
            order.setShippingAddress(updateOrderDto.getShippingAddress());
        }
        if (Objects.nonNull(updateOrderDto.getPaymentDetails())) {
            order.setPaymentDetails(updateOrderDto.getPaymentDetails());
        }
        if (Objects.nonNull(updateOrderDto.getPaymentDate())) {
            order.setPaymentDate(updateOrderDto.getPaymentDate());
        }
        if (Objects.nonNull(updateOrderDto.getShippedDate())) {
            order.setShippedDate(updateOrderDto.getShippedDate());
        }
        if (Objects.nonNull(updateOrderDto.getDeliveredDate())) {
            order.setDeliveredDate(updateOrderDto.getDeliveredDate());
        }

        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void deleteOrder(Long id) {
        Order document = findOrderById(id);
        orderRepository.delete(document);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));
    }
}
