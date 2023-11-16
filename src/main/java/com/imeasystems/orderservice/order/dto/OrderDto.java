package com.imeasystems.orderservice.order.dto;

import com.imeasystems.orderservice.customer.dto.CustomerDto;
import com.imeasystems.orderservice.order.dto.orderitem.OrderItemDto;
import com.imeasystems.orderservice.order.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;

    private CustomerDto customer;

    private OrderStatus status;

    private List<OrderItemDto> orderItems;

    private String shippingAddress;

    private String paymentDetails;

    private LocalDateTime orderCreationDate;

    private LocalDateTime paymentDate;

    private LocalDateTime shippedDate;

    private LocalDateTime deliveredDate;
}
