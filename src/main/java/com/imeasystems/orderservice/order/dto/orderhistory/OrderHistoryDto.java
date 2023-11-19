package com.imeasystems.orderservice.order.dto.orderhistory;

import com.imeasystems.orderservice.order.util.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderHistoryDto {

    @NotNull
    private Long id;

    @NotNull
    private Long orderId;

    @NotNull
    private LocalDateTime modifiedDate;

    @NotNull
    private OrderStatus orderStatus;

    public OrderHistoryDto(Long orderId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }
}
