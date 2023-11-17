package com.imeasystems.orderservice.order.mapper;

import com.imeasystems.orderservice.order.dto.orderitem.OrderItemDto;
import com.imeasystems.orderservice.order.dto.orderitem.UpdateOrderItemDto;
import com.imeasystems.orderservice.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "order.id", target = "orderId")
    OrderItemDto orderItemToOrderItemDto(OrderItem item);

    List<OrderItemDto> orderItemListToOrderItemDtoList(List<OrderItem> items);

    List<OrderItem> updateOrderItemDtoListToOrderItemList(List<UpdateOrderItemDto> items);
}
