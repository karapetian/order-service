package com.imeasystems.orderservice.order.mapper;

import com.imeasystems.orderservice.order.dto.orderhistory.OrderHistoryDto;
import com.imeasystems.orderservice.order.entity.OrderHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderHistoryMapper {

    @Mapping(source = "order.id", target = "orderId")
    OrderHistoryDto orderHistoryToOrderHistoryDto(OrderHistory history);

    List<OrderHistoryDto> orderHistoryListToOrderHistoryDtoList(List<OrderHistory> orderHistories);
}
