package com.imeasystems.orderservice.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerDto {

    private String name;

    private String surname;

    @Email(regexp = "^(.+)@(.+)$")
    private String email;

    @Pattern(regexp = "^\\+{0,1}[0-9]{8,}$")
    private String phoneNumber;
}