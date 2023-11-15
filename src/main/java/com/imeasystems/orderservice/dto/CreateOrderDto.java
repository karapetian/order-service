package com.imeasystems.orderservice.dto;

import com.imeasystems.orderservice.util.OrderStatus;

import java.time.LocalDateTime;

public class CreateOrderDto {

    private Long customerId;

    private OrderStatus status;

//    private List<OrderItem> orderItems;

    private String shippingAddress;

    private String paymentDetails;

    private LocalDateTime orderCreationDate;
}
