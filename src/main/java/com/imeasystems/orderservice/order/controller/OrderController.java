package com.imeasystems.orderservice.order.controller;

import com.imeasystems.orderservice.order.dto.CreateOrderDto;
import com.imeasystems.orderservice.order.dto.OrderDto;
import com.imeasystems.orderservice.order.dto.OrderResponse;
import com.imeasystems.orderservice.order.dto.UpdateOrderDto;
import com.imeasystems.orderservice.order.dto.orderhistory.OrderHistoryDto;
import com.imeasystems.orderservice.order.service.OrderService;
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

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    public static final String ID = "id";

    private final OrderService orderService;

    @Operation(summary = "Create order")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @Valid @NotNull @RequestBody final CreateOrderDto createOrderDto) {
        OrderDto order = orderService.createOrder(createOrderDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(order);
    }

    @Operation(summary = "Get order by id")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@Valid @NotNull @Positive @PathVariable final Long id) {
        OrderDto orderDto = orderService.getOrder(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDto);
    }
    @Operation(summary = "Get all orders, Adjust default params - page(0), size(10), sort(ASC)")
    @GetMapping
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(value = "page", defaultValue = "0") @Valid @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Valid @Positive @Min(1) int size,
            @RequestParam(value = "sort", defaultValue = "ASC") String sort
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sort), ID);
        OrderResponse orderResponse = orderService.getAllOrders(pageRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderResponse);
    }

    @Operation(summary = "Update order by id")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@Valid @NotNull @Positive @PathVariable final Long id) {
        orderService.deleteOrder(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Operation(summary = "Get the history of the Order")
    @GetMapping("/{id}/history")
    public ResponseEntity<List<OrderHistoryDto>> getOrderHistory(@Valid @NotNull @Positive @PathVariable final Long id) {
        List<OrderHistoryDto> orderHistories = orderService.getOrderHistories(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderHistories);
    }
}