package com.imeasystems.orderservice.order.service.impl;

import com.imeasystems.orderservice.customer.entity.Customer;
import com.imeasystems.orderservice.customer.repository.CustomerRepository;
import com.imeasystems.orderservice.order.dto.CreateOrderDto;
import com.imeasystems.orderservice.order.dto.OrderDto;
import com.imeasystems.orderservice.order.dto.OrderResponse;
import com.imeasystems.orderservice.order.dto.UpdateOrderDto;
import com.imeasystems.orderservice.order.dto.orderhistory.OrderHistoryDto;
import com.imeasystems.orderservice.order.dto.orderitem.OrderItemDto;
import com.imeasystems.orderservice.order.entity.Order;
import com.imeasystems.orderservice.order.entity.OrderHistory;
import com.imeasystems.orderservice.order.entity.OrderItem;
import com.imeasystems.orderservice.order.mapper.OrderHistoryMapper;
import com.imeasystems.orderservice.order.mapper.OrderItemMapper;
import com.imeasystems.orderservice.order.mapper.OrderMapper;
import com.imeasystems.orderservice.order.repository.OrderHistoryRepository;
import com.imeasystems.orderservice.order.repository.OrderItemRepository;
import com.imeasystems.orderservice.order.repository.OrderRepository;
import com.imeasystems.orderservice.order.service.OrderService;
import com.imeasystems.orderservice.order.util.OrderStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final OrderItemRepository orderItemRepository;

    private final OrderHistoryRepository orderHistoryRepository;

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    private final OrderHistoryMapper orderHistoryMapper;

    @Transactional
    @Override
    public OrderDto createOrder(CreateOrderDto createOrderDto) {
        //save order
        Order order = orderMapper.createOrderDtoToOrder(createOrderDto);

        Customer customer = customerRepository.findById(createOrderDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer with id " + createOrderDto.getCustomerId() + " not found"));

        order.setCustomer(customer);
        order.setCurrentStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        //save orderItems
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach(item -> item.setOrder(savedOrder));
        List<OrderItem> savedItems = orderItemRepository.saveAll(orderItems);

        //save OrderHistory
        OrderHistory currentState = new OrderHistory(savedOrder, order.getCurrentStatus());
        orderHistoryRepository.save(currentState);

        //map to dtos
        List<OrderItemDto> itemDtos = orderItemMapper.orderItemListToOrderItemDtoList(savedItems);
        OrderDto orderDto = orderMapper.orderToOrderDto(savedOrder);
        orderDto.setOrderItems(itemDtos);
        log.info("Successfully created Order: {}", orderDto);
        return orderDto;
    }

    @Override
    public OrderDto getOrder(Long id) {
        return orderMapper.orderToOrderDto(findOrderById(id));
    }

    @Override
    public OrderResponse getAllOrders(PageRequest pageRequest) {
        Page<Order> orderPage = orderRepository.findAll(pageRequest);
        List<OrderDto> orderDtos = orderMapper.orderListToOrderDtoList(orderPage.getContent());

        return OrderResponse.builder()
                .totalItems(orderPage.getTotalElements())
                .orders(orderDtos)
                .totalPages(orderPage.getTotalPages())
                .currentPage(orderPage.getNumber())
                .build();
    }

    @Transactional
    @Override
    public void updateOrder(Long id, UpdateOrderDto updateOrderDto) {
        log.info("Updating Order with id:{}", id);
        Order order = findOrderById(id);

        if (!CollectionUtils.isEmpty(updateOrderDto.getOrderItems())) {
            int count = orderItemRepository.deleteAllByOrderId(id);
            log.info("{} OrderItems were deleted with orderId:{}", count, id);
            List<OrderItem> orderItems = orderItemMapper.updateOrderItemDtoListToOrderItemList(updateOrderDto.getOrderItems());
            orderItems.forEach(item -> item.setOrder(order));
            orderItemRepository.saveAll(orderItems);
            log.info("New OrderItems saved for Order with orderId:{}", id);
        }
        if (Objects.nonNull(updateOrderDto.getCustomerId())) {
            Customer customer = customerRepository.getReferenceById(updateOrderDto.getCustomerId());
            order.setCustomer(customer);
        }
        if (Objects.nonNull(updateOrderDto.getCurrentStatus())) {
            order.setCurrentStatus(updateOrderDto.getCurrentStatus());
            OrderHistory currentState = new OrderHistory(order, updateOrderDto.getCurrentStatus());
            orderHistoryRepository.save(currentState);
        }
        if (Objects.nonNull(updateOrderDto.getShippingAddress())) {
            order.setShippingAddress(updateOrderDto.getShippingAddress());
        }
        if (Objects.nonNull(updateOrderDto.getPaymentDetails())) {
            order.setPaymentDetails(updateOrderDto.getPaymentDetails());
        }
        if (Objects.nonNull(updateOrderDto.getPaymentDate())) {
            order.setPaymentDate(updateOrderDto.getPaymentDate());
        }
        if (Objects.nonNull(updateOrderDto.getShippedDate())) {
            order.setShippedDate(updateOrderDto.getShippedDate());
        }
        if (Objects.nonNull(updateOrderDto.getDeliveredDate())) {
            order.setDeliveredDate(updateOrderDto.getDeliveredDate());
        }

        orderRepository.save(order);
        log.info("Successfully updated Order with id:{}", id);
    }

    @Transactional
    @Override
    public void deleteOrder(Long id) {
        Order customer = findOrderById(id);
        orderRepository.delete(customer);
        log.info("Successfully deleted Order and OrderItems with orderId:{}", id);
    }

    @Override
    public List<OrderHistoryDto> getOrderHistories(Long orderId) {
        List<OrderHistory> allByOrderId = orderHistoryRepository.findAllByOrderId(orderId);
        return orderHistoryMapper.orderHistoryListToOrderHistoryDtoList(allByOrderId);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));
    }
}
