package com.imeasystems.orderservice.order.mapper;

import com.imeasystems.orderservice.order.dto.CreateOrderDto;
import com.imeasystems.orderservice.order.dto.OrderDto;
import com.imeasystems.orderservice.order.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto orderToOrderDto(Order order);

    Order createOrderDtoToOrder(CreateOrderDto createOrderDto);

    List<OrderDto> orderListToOrderDtoList(List<Order> orders);
}
