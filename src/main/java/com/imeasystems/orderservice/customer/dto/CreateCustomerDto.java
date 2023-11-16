package com.imeasystems.orderservice.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCustomerDto {
    @NotNull
    private String name;

    private String surname;

    @Email(regexp = "^(.+)@(.+)$")
    @NotNull
    private String email;

    @Pattern(regexp = "^\\+{0,1}[0-9]{8,}$")
    @NotNull
    private String phoneNumber;
}