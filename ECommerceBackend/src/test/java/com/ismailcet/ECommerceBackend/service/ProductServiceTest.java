package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.dto.ProductDto;
import com.ismailcet.ECommerceBackend.dto.converter.ProductDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreateProductRequest;
import com.ismailcet.ECommerceBackend.entity.Product;
import com.ismailcet.ECommerceBackend.repository.CategoryRepository;
import com.ismailcet.ECommerceBackend.repository.ProductRepository;
import com.ismailcet.ECommerceBackend.repository.SizeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    private ProductService productService;
    private ProductRepository productRepository;
    private JwtFilter jwtFilter;
    private ProductDtoConverter productDtoConverter;
    private SizeRepository sizeRepository;
    private CategoryRepository categoryRepository;
    @BeforeEach
    public void setUp(){
        productRepository = mock(ProductRepository.class);
        jwtFilter  = mock(JwtFilter.class);
        productDtoConverter  = mock(ProductDtoConverter.class);
        sizeRepository = mock(SizeRepository.class);
        categoryRepository = mock(CategoryRepository.class);

        productService = new ProductService(productRepository,
                jwtFilter,
                productDtoConverter,
                sizeRepository,
                categoryRepository);
    }

    @Test
    public void testCreateProduct_whenUserRoleIsAdmin_shouldCreateProductSuccessfullyAndReturnProductDto(){
        CreateProductRequest given = CreateProductRequest.builder()
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .build();
        Product product = Product.builder()
                .id(1)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .build();

        ProductDto excepted = ProductDto.builder()
                .id(1)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .build();

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productDtoConverter.convert(any(Product.class))).thenReturn(excepted);

        ProductDto actual = productService.createProduct(given);

        assertEquals(excepted, actual);
        assertEquals(given.getName(), actual.getName());


        verify(jwtFilter).isAdmin();
        verify(productRepository).save(any(Product.class));
        verify(productDtoConverter).convert(any(Product.class));

    }
}