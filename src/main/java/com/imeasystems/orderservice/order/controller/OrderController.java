package com.imeasystems.orderservice.order.controller;

import com.imeasystems.orderservice.exception.handler.dto.ApiError;
import com.imeasystems.orderservice.order.dto.CreateOrderDto;
import com.imeasystems.orderservice.order.dto.OrderDto;
import com.imeasystems.orderservice.order.dto.OrderResponse;
import com.imeasystems.orderservice.order.dto.UpdateOrderDto;
import com.imeasystems.orderservice.order.dto.orderhistory.OrderHistoryDto;
import com.imeasystems.orderservice.order.service.OrderService;
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

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "Order API", description = "Endpoints for managing order resources with their order items and history")
public class OrderController {

    public static final String ID = "id";

    private final OrderService orderService;

    @Operation(summary = "Create order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))}),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity. Invalid data provided",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))})})
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @Valid @NotNull @RequestBody final CreateOrderDto createOrderDto) {
        OrderDto order = orderService.createOrder(createOrderDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(order);
    }

    @Operation(summary = "Get order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Order",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))}),
            @ApiResponse(responseCode = "400", description = "Provided id must be grater than 0",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@Valid @NotNull @Positive @PathVariable final Long id) {
        OrderDto orderDto = orderService.getOrder(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDto);
    }
    @Operation(summary = "Get all orders, Adjust default params - page(0), size(10), sort(ASC)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of orders",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))})})
    @GetMapping
    public ResponseEntity<OrderResponse> getAllOrders(
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
        OrderResponse orderResponse = orderService.getAllOrders(pageRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderResponse);
    }

    @Operation(summary = "Update order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the Order"),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrder(
            @Valid @NotNull @Positive @PathVariable final Long id,
            @Valid @NotNull @RequestBody final UpdateOrderDto updateOrderDto) {
        orderService.updateOrder(id, updateOrderDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(summary = "Delete order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the Order"),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@Valid @NotNull @Positive @PathVariable final Long id) {
        orderService.deleteOrder(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(summary = "Get the history of the Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the order's history",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderHistoryDto.class))}),
            @ApiResponse(responseCode = "400", description = "Provided id must be grater than 0",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))})})
    @GetMapping("/{id}/history")
    public ResponseEntity<List<OrderHistoryDto>> getOrderHistory(@Valid @NotNull @Positive @PathVariable final Long id) {
        List<OrderHistoryDto> orderHistories = orderService.getOrderHistories(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderHistories);
    }
}