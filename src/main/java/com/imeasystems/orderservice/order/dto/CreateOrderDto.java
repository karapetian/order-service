package com.imeasystems.orderservice.order.dto;

import com.imeasystems.orderservice.order.dto.orderitem.CreateOrderItemDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {

    @NotNull
    private Long customerId;

    @NotEmpty
    private List<CreateOrderItemDto> orderItems;

    @NotNull
    private String shippingAddress;

    private String paymentDetails;
}
