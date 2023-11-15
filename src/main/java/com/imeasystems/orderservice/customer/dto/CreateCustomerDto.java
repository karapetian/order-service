package com.imeasystems.orderservice.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCustomerDto {
    @NotNull
    private String name;

    private String surname;

    private String email;

    private String phoneNumber;
}