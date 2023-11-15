package com.imeasystems.orderservice.customer.service.impl;

import com.imeasystems.orderservice.customer.dto.CreateCustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerResponse;
import com.imeasystems.orderservice.customer.dto.UpdateCustomerDto;
import com.imeasystems.orderservice.customer.entity.Customer;
import com.imeasystems.orderservice.customer.mapper.CustomerMapper;
import com.imeasystems.orderservice.customer.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    private static final Long ID = 10001L;

    private static final String NAME = "John";

    private static final String SURNAME = "Smith";

    private static final String EMAIL = "johnsmith@gmail.com";

    private static final String PHONE_NUMBER = "+094555555";

    private static final Long ID_2 = 20002L;

    private static final String NAME_2 = "Anna";

    private static final String SURNAME_2 = "Martin";

    private static final String EMAIL_2 = "annamartin@gmail.com";

    private static final String PHONE_NUMBER_2 = "+094222222";

    @Test
    void createCustomerSuccessTest() {
        CreateCustomerDto createCustomerDto = new CreateCustomerDto();
        Customer customer = new Customer();
        when(customerMapper.createCustomerDtoToCustomer(createCustomerDto)).thenReturn(customer);

        customerService.createCustomer(createCustomerDto);

        verify(customerMapper, times(1)).createCustomerDtoToCustomer(createCustomerDto);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void getCustomerSuccessTest() {
        Customer customer = new Customer();
        CustomerDto expectedCustomerDto = new CustomerDto(ID, NAME, SURNAME, EMAIL, PHONE_NUMBER);

        when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerDto(customer)).thenReturn(expectedCustomerDto);

        CustomerDto actualCustomerDto = customerService.getCustomer(ID);

        verify(customerRepository, times(1)).findById(ID);
        verify(customerMapper, times(1)).customerToCustomerDto(customer);

        assertNotNull(expectedCustomerDto);
        Assertions.assertEquals(expectedCustomerDto.getId(), actualCustomerDto.getId());
        Assertions.assertEquals(expectedCustomerDto.getName(), actualCustomerDto.getName());
        Assertions.assertEquals(expectedCustomerDto.getSurname(), actualCustomerDto.getSurname());
        Assertions.assertEquals(expectedCustomerDto.getEmail(), actualCustomerDto.getEmail());
        Assertions.assertEquals(expectedCustomerDto.getPhoneNumber(), actualCustomerDto.getPhoneNumber());
    }

    @Test
    void getCustomerFailedTest() {
        when(customerRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.getCustomer(ID));

        verify(customerRepository, times(1)).findById(ID);
    }

    @Test
    void getAllCategoriesSuccessTest() {
        PageRequest pageRequest = buildPageDto();
        List<Customer> customers = List.of(buildCustomer1(), buildCustomer2());
        List<CustomerDto> customerDtos = List.of(buildCustomerDto1(), buildCustomerDto2());
        Page<Customer> customerPage = new PageImpl<>(customers);

        when(customerRepository.findAll(any(PageRequest.class))).thenReturn(customerPage);
        when(customerMapper.customerListToCustomerDtoList(customers)).thenReturn(customerDtos);

        CustomerResponse customerResponse = customerService.getAllCustomers(pageRequest);

        verify(customerRepository, times(1)).findAll(pageRequest);
        verify(customerMapper, times(1)).customerListToCustomerDtoList(customers);

        assertNotNull(customerResponse);
        assertNotNull(customerResponse.getCustomers());
        assertEquals(2, customerResponse.getTotalItems());
        assertEquals(0, customerResponse.getCurrentPage());
        assertEquals(1, customerResponse.getTotalPages());
        assertEquals(2, customerResponse.getCustomers().size());

        List<CustomerDto> customerSearchResult = customerResponse.getCustomers();
        CustomerDto customerDtoSearchResult1 = customerSearchResult.get(0);
        assertEquals(buildCustomerDto1().getId(), customerDtoSearchResult1.getId());
        assertEquals(buildCustomerDto1().getName(), customerDtoSearchResult1.getName());
        assertEquals(buildCustomerDto1().getSurname(), customerDtoSearchResult1.getSurname());
        assertEquals(buildCustomerDto1().getEmail(), customerDtoSearchResult1.getEmail());
        assertEquals(buildCustomerDto1().getPhoneNumber(), customerDtoSearchResult1.getPhoneNumber());

        CustomerDto customerDtoSearchResult2 = customerSearchResult.get(1);
        assertEquals(buildCustomerDto2().getId(), customerDtoSearchResult2.getId());
        assertEquals(buildCustomerDto2().getName(), customerDtoSearchResult2.getName());
        assertEquals(buildCustomerDto2().getSurname(), customerDtoSearchResult2.getSurname());
        assertEquals(buildCustomerDto2().getEmail(), customerDtoSearchResult2.getEmail());
        assertEquals(buildCustomerDto2().getPhoneNumber(), customerDtoSearchResult2.getPhoneNumber());
    }

    @Test
    void updateCustomerSuccessTest() {
        UpdateCustomerDto updateCustomerDto = buildUpdateCustomerDto();
        Customer customer = buildCustomer1();

        when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));

        customerService.updateCustomer(ID, updateCustomerDto);

        verify(customerRepository, times(1)).findById(ID);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void updateCustomerFailedTest() {
        UpdateCustomerDto updateCustomerDto = buildUpdateCustomerDto();

        when(customerRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.updateCustomer(ID, updateCustomerDto));
    }

    @Test
    void deleteCustomerSuccessTest() {
        Customer customer = buildCustomer1();

        when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(ID);

        verify(customerRepository, times(1)).findById(ID);
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void deleteCustomerFailedTest() {
        when(customerRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomer(ID));
    }

    private CustomerDto buildCustomerDto1() {
        return new CustomerDto(ID, NAME, SURNAME, EMAIL, PHONE_NUMBER);
    }

    private CustomerDto buildCustomerDto2() {
        return new CustomerDto(ID_2, NAME_2, SURNAME_2, EMAIL_2, PHONE_NUMBER_2);
    }

    private Customer buildCustomer1() {
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setName(NAME);
        customer.setSurname(SURNAME);
        customer.setEmail(EMAIL);
        customer.setPhoneNumber(PHONE_NUMBER);
        return customer;
    }

    private Customer buildCustomer2() {
        Customer customer = new Customer();
        customer.setId(ID_2);
        customer.setName(NAME_2);
        customer.setSurname(SURNAME_2);
        customer.setEmail(EMAIL_2);
        customer.setPhoneNumber(PHONE_NUMBER_2);
        return customer;
    }

    private PageRequest buildPageDto() {
        return PageRequest.of(0, 10, Sort.Direction.ASC, "id");
    }

    private UpdateCustomerDto buildUpdateCustomerDto() {
        return new UpdateCustomerDto(NAME, SURNAME, "updated-email", "updated-phone");
    }
}