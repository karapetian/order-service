package com.imeasystems.orderservice.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {

    private Long customerId;

//    private List<OrderItem> orderItems;

    private String shippingAddress;

    private String paymentDetails;
}
