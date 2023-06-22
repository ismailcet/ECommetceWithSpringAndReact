package com.ismailcet.ECommerceBackend.dto;

import com.ismailcet.ECommerceBackend.entity.Order;
import com.ismailcet.ECommerceBackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {

    private Integer id;
    private double price;
    private UserDto user;
    private OrderDto order;
    private Date paymentDate;
}
