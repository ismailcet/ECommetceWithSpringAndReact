package com.ismailcet.ECommerceBackend.dto.converter;

import com.ismailcet.ECommerceBackend.dto.ProductDto;
import com.ismailcet.ECommerceBackend.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoConverter {
    public ProductDto convert(Product product){
        ProductDto productDto = ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .color(product.getColor())
                .photoUrl(product.getPhotoUrl())
                .price(product.getPrice())
                .stock(product.getStock())
                .categoriesProduct(product.getCategoriesProduct())
                .sizesProduct(product.getSizesProduct())
                .productImageList(product.getProductImageList())
                .build();
        return productDto;
    }
}
