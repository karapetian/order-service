package com.imeasystems.orderservice.order.dto.orderitem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private Long id;

    private Long orderId;

    private String product;

    private int quantity;
}
