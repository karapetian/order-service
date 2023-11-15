package com.imeasystems.orderservice.customer.dto;

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
public class CustomerResponse {

    private long totalItems;

    private List<CustomerDto> customers = new ArrayList<>();

    private long totalPages;

    private long currentPage;
}
