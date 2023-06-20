package com.ismailcet.ECommerceBackend.dto;

import com.ismailcet.ECommerceBackend.entity.CargoStatus;
import com.ismailcet.ECommerceBackend.entity.OrderItem;
import com.ismailcet.ECommerceBackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Date createdDate;
    private CargoStatus cargoStatus;
    private String address;
    private double amount;
    private UserDto user;
    private List<OrderItemDtoResponse> orderItems;
}
