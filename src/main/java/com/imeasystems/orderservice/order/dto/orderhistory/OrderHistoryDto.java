package com.imeasystems.orderservice.order.dto.orderhistory;

import com.imeasystems.orderservice.order.util.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderHistoryDto {

    @NotNull
    private Long orderId;

    @NotNull
    private LocalDateTime modifiedDate;

    @NotNull
    private OrderStatus orderStatus;
}
