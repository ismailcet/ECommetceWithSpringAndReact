package com.ismailcet.ECommerceBackend.dto.response;

import com.ismailcet.ECommerceBackend.entity.Category;
import com.ismailcet.ECommerceBackend.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductResponse {
    private Integer id;
    private String photoUrl;
    private String name;
    private Double price;
    private String color;
    private Set<Size> sizesProduct = new HashSet<>();
    private Set<Category> categoriesProduct = new HashSet<>();
}
