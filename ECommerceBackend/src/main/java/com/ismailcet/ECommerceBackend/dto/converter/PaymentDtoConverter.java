package com.ismailcet.ECommerceBackend.dto.converter;

import com.ismailcet.ECommerceBackend.dto.PaymentDto;
import com.ismailcet.ECommerceBackend.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentDtoConverter {

    private final UserDtoConverter userDtoConverter;
    private final OrderDtoConverter orderDtoConverter;

    public PaymentDtoConverter(UserDtoConverter userDtoConverter, OrderDtoConverter orderDtoConverter) {
        this.userDtoConverter = userDtoConverter;
        this.orderDtoConverter = orderDtoConverter;
    }

    public PaymentDto convert(Payment payment){
        return PaymentDto.builder()
                .id(payment.getId())
                .price(payment.getPrice())
                .user(userDtoConverter.convert(payment.getUser()))
                .order(orderDtoConverter.convert(payment.getOrder()))
                .paymentDate(new Date())
                .build();
    }
}
