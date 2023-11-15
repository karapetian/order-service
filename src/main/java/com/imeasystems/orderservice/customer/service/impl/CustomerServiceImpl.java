package com.imeasystems.orderservice.customer.service.impl;

import com.imeasystems.orderservice.customer.dto.CreateCustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerResponse;
import com.imeasystems.orderservice.customer.dto.UpdateCustomerDto;
import com.imeasystems.orderservice.customer.entity.Customer;
import com.imeasystems.orderservice.customer.mapper.CustomerMapper;
import com.imeasystems.orderservice.customer.repository.CustomerRepository;
import com.imeasystems.orderservice.customer.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    
    private final CustomerMapper customerMapper;
    
    @Transactional
    @Override
    public CustomerDto createCustomer(CreateCustomerDto createCustomerDto) {
        Customer customer = customerMapper.createCustomerDtoToCustomer(createCustomerDto);
        return customerMapper.customerToCustomerDto(customerRepository.save(customer));
    }

    @Override
    public CustomerDto getCustomer(Long id) {
        return customerMapper.customerToCustomerDto(findCustomerById(id));
    }

    @Override
    public CustomerResponse getAllCustomers(PageRequest pageRequest) {
        Page<Customer> customerPage = customerRepository.findAll(pageRequest);
        List<CustomerDto> customerDtos = customerMapper.customerListToCustomerDtoList(customerPage.getContent());

        return CustomerResponse.builder()
                .totalItems(customerPage.getTotalElements())
                .customers(customerDtos)
                .totalPages(customerPage.getTotalPages())
                .currentPage(customerPage.getNumber())
                .build();
    }

    @Transactional
    @Override
    public void updateCustomer(Long id, UpdateCustomerDto updateCustomerDto) {
        Customer customer = findCustomerById(id);

        if (Objects.nonNull(updateCustomerDto.getName())) {
            customer.setName(updateCustomerDto.getName());
        }
        if (Objects.nonNull(updateCustomerDto.getSurname())) {
            customer.setSurname(updateCustomerDto.getSurname());
        }
        if (Objects.nonNull(updateCustomerDto.getEmail())) {
            customer.setEmail(updateCustomerDto.getEmail());
        }
        if (Objects.nonNull(updateCustomerDto.getPhoneNumber())) {
            customer.setPhoneNumber(updateCustomerDto.getPhoneNumber());
        }

        customerRepository.save(customer);
    }

    @Transactional
    @Override
    public void deleteCustomer(Long id) {
        Customer customer = findCustomerById(id);
        customerRepository.delete(customer);
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id " + id + " not found"));
    }
}