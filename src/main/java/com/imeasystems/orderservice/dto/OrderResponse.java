package com.imeasystems.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private long totalItems;

    private List<OrderDto> orders = new ArrayList<>();

    private long totalPages;

    private long currentPage;
}
