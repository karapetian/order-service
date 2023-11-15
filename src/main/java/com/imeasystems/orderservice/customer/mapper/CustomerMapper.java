package com.imeasystems.orderservice.customer.mapper;

import com.imeasystems.orderservice.customer.dto.CreateCustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerDto;
import com.imeasystems.orderservice.customer.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(Customer customer);

    Customer createCustomerDtoToCustomer(CreateCustomerDto createCustomerDto);

    List<CustomerDto> customerListToCustomerDtoList(List<Customer> customers);
}
