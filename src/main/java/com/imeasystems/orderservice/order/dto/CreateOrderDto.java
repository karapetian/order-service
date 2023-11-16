package com.imeasystems.orderservice.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {

    @NotNull
    private Long customerId;

//    private List<OrderItem> orderItems;

    @NotNull
    private String shippingAddress;

    private String paymentDetails;
}
