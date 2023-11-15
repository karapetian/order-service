package com.imeasystems.orderservice.dto;

import com.imeasystems.orderservice.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;

    private CustomerDto customer;

    private OrderStatus status;

//    private List<OrderItem> orderItems;

    private String shippingAddress;

    private String paymentDetails;

    private LocalDateTime orderCreationDate;

    private LocalDateTime paymentDate;

    private LocalDateTime shippedDate;

    private LocalDateTime deliveredDate;
}
