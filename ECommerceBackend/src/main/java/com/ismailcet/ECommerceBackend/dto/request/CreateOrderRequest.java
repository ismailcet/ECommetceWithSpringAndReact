package com.ismailcet.ECommerceBackend.dto.request;

import com.ismailcet.ECommerceBackend.dto.OrderItemDto;
import com.ismailcet.ECommerceBackend.entity.CargoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    private Date createdDate = new Date();
    private CargoStatus cargoStatus ;
    private String address;
    private double amount;
    private Integer userId;
    private List<OrderItemDto> orderItems;
}
