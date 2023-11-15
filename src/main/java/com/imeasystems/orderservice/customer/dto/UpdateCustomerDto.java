package com.imeasystems.orderservice.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerDto {

    private String name;

    private String surname;

    private String email;

    private String phoneNumber;
}