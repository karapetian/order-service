package com.imeasystems.orderservice.order.dto;

import com.imeasystems.orderservice.order.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDto {

    private Long customerId;

    private OrderStatus status;

//    private List<OrderItem> orderItems;

    private String shippingAddress;

    private String paymentDetails;

    private LocalDateTime paymentDate;

    private LocalDateTime shippedDate;

    private LocalDateTime deliveredDate;
}
