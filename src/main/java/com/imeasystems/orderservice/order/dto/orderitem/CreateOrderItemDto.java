package com.imeasystems.orderservice.order.dto.orderitem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderItemDto {

    private String product;

    private int quantity;
}
