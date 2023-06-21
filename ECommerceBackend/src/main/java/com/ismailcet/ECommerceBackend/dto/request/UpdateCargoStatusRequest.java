package com.ismailcet.ECommerceBackend.dto.request;

import com.ismailcet.ECommerceBackend.entity.CargoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCargoStatusRequest {
    private CargoStatus cargoStatus;
}
