package com.ismailcet.ECommerceBackend.dto.converter;

import com.ismailcet.ECommerceBackend.dto.OrderDto;
import com.ismailcet.ECommerceBackend.entity.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderDtoConverter {
    private final UserDtoConverter converter;
    private final OrderItemDtoConverter orderItemDtoConverter;

    public OrderDtoConverter(UserDtoConverter converter, OrderItemDtoConverter orderItemDtoConverter) {
        this.converter = converter;
        this.orderItemDtoConverter = orderItemDtoConverter;
    }

    public OrderDto convert(Order order){
        OrderDto orderDto = OrderDto.builder()
                .address(order.getAddress())
                .cargoStatus(order.getCargoStatus())
                .amount(order.getAmount())
                .createdDate(order.getCreatedDate())
                .user(converter.convert(order.getUser()))
                .orderItems(order.getOrderItems().stream().map(e -> orderItemDtoConverter.convert(e)).collect(Collectors.toList()))
                .build();

        return orderDto;
    }
}
