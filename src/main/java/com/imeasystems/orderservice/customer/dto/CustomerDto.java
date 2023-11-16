package com.imeasystems.orderservice.customer.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private Long id;

    private String name;

    private String surname;

    @Email(regexp = "^(.+)@(.+)$")
    private String email;

    private String phoneNumber;
}
