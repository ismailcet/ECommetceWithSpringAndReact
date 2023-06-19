package com.ismailcet.ECommerceBackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {
    private String name;
    private Double price;
    private String color;
    private String photoUrl;
    private Integer stock;
}
