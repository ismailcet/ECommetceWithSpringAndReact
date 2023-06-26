package com.ismailcet.ECommerceBackend.dto;

import com.ismailcet.ECommerceBackend.entity.Category;
import com.ismailcet.ECommerceBackend.entity.ProductImage;
import com.ismailcet.ECommerceBackend.entity.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ProductDto {
    private Integer id;
    private String photoUrl;
    private String name;
    private Double price;
    private String color;
    private Integer stock;
    private Set<Size> sizesProduct = new HashSet<>();
    private Set<Category> categoriesProduct = new HashSet<>();
    private List<ProductImage> productImageList ;
}
