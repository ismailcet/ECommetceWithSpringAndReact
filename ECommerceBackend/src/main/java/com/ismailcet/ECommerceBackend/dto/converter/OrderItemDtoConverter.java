package com.ismailcet.ECommerceBackend.dto.converter;

import com.ismailcet.ECommerceBackend.dto.OrderItemDtoResponse;
import com.ismailcet.ECommerceBackend.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDtoConverter {
    private final ProductDtoConverter productDtoConverter;

    public OrderItemDtoConverter(ProductDtoConverter productDtoConverter) {
        this.productDtoConverter = productDtoConverter;
    }

    public OrderItemDtoResponse convert(OrderItem orderItem){
        OrderItemDtoResponse orderItemDtoResponse = OrderItemDtoResponse.builder()
                .quantity(orderItem.getQuantity())
                .product(productDtoConverter.convert(orderItem.getProduct()))
                .build();
        return orderItemDtoResponse;
    }
}
