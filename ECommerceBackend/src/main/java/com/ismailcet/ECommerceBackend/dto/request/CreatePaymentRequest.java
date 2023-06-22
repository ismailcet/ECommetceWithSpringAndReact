package com.ismailcet.ECommerceBackend.dto.request;

import com.ismailcet.ECommerceBackend.dto.OrderDto;
import com.ismailcet.ECommerceBackend.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePaymentRequest {
    private long creditCart;
    private Integer cvc;
    private String date;
    private double price;
    private Integer userId;
    private Integer orderId;
    private Date paymentDate =new Date();
}
