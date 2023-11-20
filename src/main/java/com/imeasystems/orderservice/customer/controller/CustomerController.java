package com.imeasystems.orderservice.customer.controller;

import com.imeasystems.orderservice.customer.dto.CreateCustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerDto;
import com.imeasystems.orderservice.customer.dto.CustomerResponse;
import com.imeasystems.orderservice.customer.dto.UpdateCustomerDto;
import com.imeasystems.orderservice.customer.service.CustomerService;
import com.imeasystems.orderservice.exception.handler.dto.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer API", description = "Endpoints for managing customer resources")
public class CustomerController {

    public static final String ID = "id";

    private final CustomerService customerService;

    @Operation(summary = "Create customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer successfully created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDto.class))}),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity. Invalid data provided",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))})})
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Parameter(description = "Data to create a Customer")
            @Valid @NotNull @RequestBody final CreateCustomerDto createCustomerDto) {
        CustomerDto customer = customerService.createCustomer(createCustomerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customer);
    }

    @Operation(summary = "Get customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Customer",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDto.class))}),
            @ApiResponse(responseCode = "400", description = "Provided id must be grater than 0",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@Parameter(description = "Customer id")
            @Valid @NotNull @Positive @PathVariable final Long id) {
        CustomerDto customerDto = customerService.getCustomer(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDto);
    }

    @Operation(summary = "Get all customers, Adjust default params - page(0), size(10), sort(ASC)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of customers",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))})})
    @GetMapping
    public ResponseEntity<CustomerResponse> getAllCustomers(
            @RequestParam(value = "page", defaultValue = "0")
            @Parameter(description = "Must be greater than or equal to 0")
            @Valid @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10")
            @Parameter(description = "Must be greater than or equal to 1")
            @Valid @Positive @Min(1) int size,
            @RequestParam(value = "sort", defaultValue = "ASC")
            @Parameter(description = "Must be 'ASC' or 'DESC', case insensitive")
            @Valid @Pattern(regexp = "((?i)\\basc\\b)|((?i)\\bdesc\\b)") String sort
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sort), ID);
        CustomerResponse customerResponse = customerService.getAllCustomers(pageRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerResponse);
    }

    @Operation(summary = "Update customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the Customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the Customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@Valid @NotNull @Positive @PathVariable final Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}