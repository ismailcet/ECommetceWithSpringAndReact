package com.ismailcet.ECommerceBackend.dto.request;

import com.ismailcet.ECommerceBackend.entity.CargoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    private String address;
    private CargoStatus cargoStatus = CargoStatus.PENDING;
    private Integer userId;
    //private Set<Integer> products;
    private double amount;
}
