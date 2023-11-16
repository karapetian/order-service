package com.imeasystems.orderservice.customer.controller;

import com.imeasystems.orderservice.customer.dto.CreateCustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerResponse;
import com.imeasystems.orderservice.customer.dto.UpdateCustomerDto;
import com.imeasystems.orderservice.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    public static final String ID = "id";

    private final CustomerService customerService;

    @Operation(summary = "Create customer")
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(
            @Valid @NotNull @RequestBody final CreateCustomerDto createCustomerDto) {
        CustomerDto customer = customerService.createCustomer(createCustomerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customer);
    }

    @Operation(summary = "Get customer by id")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@Valid @NotNull @Positive @PathVariable final Long id) {
        CustomerDto customerDto = customerService.getCustomer(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDto);
    }

    @Operation(summary = "Get all customers, Adjust default params - page(0), size(10), sort(ASC)")
    @GetMapping
    public ResponseEntity<CustomerResponse> getAllCustomers(
            @RequestParam(value = "page", defaultValue = "0") @Valid @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Valid @Positive @Min(1) int size,
            @RequestParam(value = "sort", defaultValue = "ASC") String sort
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sort), ID);
        CustomerResponse customerResponse = customerService.getAllCustomers(pageRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerResponse);
    }

    @Operation(summary = "Update customer by id")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(
            @Valid @NotNull @Positive @PathVariable final Long id,
            @Valid @NotNull @RequestBody final UpdateCustomerDto updateCustomerDto) {
        customerService.updateCustomer(id, updateCustomerDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(summary = "Delete customer by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@Valid @NotNull @Positive @PathVariable final Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}