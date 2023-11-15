package com.imeasystems.orderservice.mapper;

import com.imeasystems.orderservice.dto.CreateOrderDto;
import com.imeasystems.orderservice.dto.OrderDto;
import com.imeasystems.orderservice.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto orderToOrderDto(Order order);

    Order createOrderDtoToOrder(CreateOrderDto createOrderDto);

    List<OrderDto> orderListToOrderDtoList(List<Order> orders);
}
