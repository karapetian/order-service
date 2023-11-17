package com.imeasystems.orderservice.order.service.impl;

import com.imeasystems.orderservice.customer.dto.CustomerDto;
import com.imeasystems.orderservice.customer.entity.Customer;
import com.imeasystems.orderservice.customer.repository.CustomerRepository;
import com.imeasystems.orderservice.order.dto.CreateOrderDto;
import com.imeasystems.orderservice.order.dto.OrderDto;
import com.imeasystems.orderservice.order.dto.OrderResponse;
import com.imeasystems.orderservice.order.dto.UpdateOrderDto;
import com.imeasystems.orderservice.order.dto.orderitem.UpdateOrderItemDto;
import com.imeasystems.orderservice.order.entity.Order;
import com.imeasystems.orderservice.order.entity.OrderItem;
import com.imeasystems.orderservice.order.mapper.OrderItemMapper;
import com.imeasystems.orderservice.order.mapper.OrderMapper;
import com.imeasystems.orderservice.order.repository.OrderItemRepository;
import com.imeasystems.orderservice.order.repository.OrderRepository;
import com.imeasystems.orderservice.order.util.OrderStatus;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
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
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    private static final Long ORDER_ID = 90001L;

    private static final Long CUSTOMER_ID = 10001L;

    private static final String ADDRESS = "some Address";

    private static final String DETAILS = "some Details";

    @Test
    void createOrderSuccessTest() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(CUSTOMER_ID);
        CreateOrderDto createOrderDto = new CreateOrderDto();
        createOrderDto.setCustomerId(CUSTOMER_ID);

        Order order = new Order();
        Order savedOrder = new Order();
        Customer customer = new Customer();
        OrderDto result = new OrderDto();

        when(orderMapper.createOrderDtoToOrder(createOrderDto)).thenReturn(order);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(orderMapper.orderToOrderDto(savedOrder)).thenReturn(result);

        orderService.createOrder(createOrderDto);

        verify(orderMapper, times(1)).createOrderDtoToOrder(createOrderDto);
        verify(orderRepository, times(1)).save(order);
        verify(orderItemRepository, times(1)).saveAll(any(List.class));
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
    }

    @Test
    void getOrderSuccessTest() {
        Order order = new Order();
        OrderDto expectedOrderDto = buildOrderDto1();

        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDto(order)).thenReturn(expectedOrderDto);

        OrderDto actualOrderDto = orderService.getOrder(ORDER_ID);

        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderMapper, times(1)).orderToOrderDto(order);

        assertNotNull(expectedOrderDto);
        assertEquals(expectedOrderDto.getId(), actualOrderDto.getId());
        assertEquals(expectedOrderDto.getStatus(), actualOrderDto.getStatus());
        assertEquals(expectedOrderDto.getPaymentDetails(), actualOrderDto.getPaymentDetails());
        assertEquals(expectedOrderDto.getCustomer().getName(), actualOrderDto.getCustomer().getName());
        assertEquals(expectedOrderDto.getCustomer().getPhoneNumber(), actualOrderDto.getCustomer().getPhoneNumber());
    }

    @Test
    void getOrderFailedTest() {
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getOrder(ORDER_ID));

        verify(orderRepository, times(1)).findById(ORDER_ID);
    }

    @Test
    void getAllOrdersSuccessTest() {
        PageRequest pageRequest = buildPageDto();
        List<Order> orders = List.of(buildOrder());
        List<OrderDto> orderDtos = List.of(buildOrderDto1());
        Page<Order> orderPage = new PageImpl<>(orders);

        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(orderPage);
        when(orderMapper.orderListToOrderDtoList(orders)).thenReturn(orderDtos);

        OrderResponse orderResponse = orderService.getAllOrders(pageRequest);

        verify(orderRepository, times(1)).findAll(pageRequest);
        verify(orderMapper, times(1)).orderListToOrderDtoList(orders);

        assertNotNull(orderResponse);
        assertNotNull(orderResponse.getOrders());
        assertEquals(1, orderResponse.getTotalItems());
        assertEquals(0, orderResponse.getCurrentPage());
        assertEquals(1, orderResponse.getTotalPages());
        assertEquals(1, orderResponse.getOrders().size());

        List<OrderDto> orderSearchResult = orderResponse.getOrders();
        OrderDto orderDtoSearchResult = orderSearchResult.get(0);
        assertEquals(buildOrderDto1().getId(), orderDtoSearchResult.getId());
        assertEquals(buildOrderDto1().getStatus(), orderDtoSearchResult.getStatus());
        assertEquals(buildOrderDto1().getShippingAddress(), orderDtoSearchResult.getShippingAddress());
        assertEquals(buildOrderDto1().getPaymentDetails(), orderDtoSearchResult.getPaymentDetails());
        assertEquals(buildOrderDto1().getCustomer().getName(), orderDtoSearchResult.getCustomer().getName());
    }

    @Test
    void updateOrderSuccessTest() {
        UpdateOrderDto updateOrderDto = buildUpdateOrderDto();
        Order order = buildOrder();

        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

        orderService.updateOrder(ORDER_ID, updateOrderDto);

        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderRepository, times(1)).save(order);
        verify(orderItemRepository, times(0)).deleteAllByOrderId(ORDER_ID);
        verify(orderItemRepository, times(0)).saveAll(any(List.class));
    }

    @Test
    void updateOrderSuccessTest_withOrderItems() {
        List<UpdateOrderItemDto> updateOrderItemList = List.of(new UpdateOrderItemDto());
        UpdateOrderDto updateOrderDto = buildUpdateOrderDto();
        updateOrderDto.setOrderItems(updateOrderItemList);

        Order order = buildOrder();
        List<OrderItem> orderItemList = List.of(new OrderItem());

        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(orderItemMapper.updateOrderItemDtoListToOrderItemList(updateOrderItemList)).thenReturn(orderItemList);

        orderService.updateOrder(ORDER_ID, updateOrderDto);

        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderRepository, times(1)).save(order);
        verify(orderItemRepository, times(1)).deleteAllByOrderId(ORDER_ID);
        verify(orderItemRepository, times(1)).saveAll(orderItemList);
    }

    @Test
    void updateOrderFailedTest() {
        UpdateOrderDto updateOrderDto = buildUpdateOrderDto();

        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.updateOrder(ORDER_ID, updateOrderDto));
    }

    @Test
    void deleteOrderSuccessTest() {
        Order order = buildOrder();

        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

        orderService.deleteOrder(ORDER_ID);

        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void deleteOrderFailedTest() {
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.deleteOrder(ORDER_ID));
    }

    private OrderDto buildOrderDto1() {
        return new OrderDto(ORDER_ID, buildCustomerDto(), OrderStatus.PENDING, Collections.emptyList(), ADDRESS,
                DETAILS, null, null, null, null);
    }

    private Order buildOrder() {
        Order order = new Order();
        order.setId(ORDER_ID);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentDetails(DETAILS);
        order.setShippingAddress(ADDRESS);
        return order;
    }

    private PageRequest buildPageDto() {
        return PageRequest.of(0, 10, Sort.Direction.ASC, "id");
    }

    private UpdateOrderDto buildUpdateOrderDto() {
        return new UpdateOrderDto(CUSTOMER_ID, OrderStatus.PENDING, Collections.emptyList(), ADDRESS,
                DETAILS, null, null, null);
    }

    private CustomerDto buildCustomerDto() {
        return new CustomerDto(CUSTOMER_ID, "John", "Smith", "john@gmail.com", "+094555555");
    }
}