package com.imeasystems.orderservice.customer.service;

import com.imeasystems.orderservice.customer.dto.CustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerResponse;
import com.imeasystems.orderservice.customer.dto.CreateCustomerDto;
import com.imeasystems.orderservice.customer.dto.UpdateCustomerDto;
import org.springframework.data.domain.PageRequest;

public interface CustomerService {

    CustomerDto createCustomer(CreateCustomerDto createCustomerDto);

    CustomerDto getCustomer(Long id);

    CustomerResponse getAllCustomers(PageRequest pageRequest);

    void updateCustomer(Long id, UpdateCustomerDto updateCustomerDto);

    void deleteCustomer(Long id);
}