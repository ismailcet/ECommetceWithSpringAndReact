package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.dto.ProductDto;
import com.ismailcet.ECommerceBackend.dto.converter.ProductDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreateProductRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateProductRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllProductsResponse;
import com.ismailcet.ECommerceBackend.dto.response.GetProductResponse;
import com.ismailcet.ECommerceBackend.entity.Category;
import com.ismailcet.ECommerceBackend.entity.Product;
import com.ismailcet.ECommerceBackend.entity.Size;
import com.ismailcet.ECommerceBackend.exception.AuthenticationException;
import com.ismailcet.ECommerceBackend.exception.CategoryNotFoundException;
import com.ismailcet.ECommerceBackend.exception.ProductNotFoundException;
import com.ismailcet.ECommerceBackend.exception.SizeNotFoundException;
import com.ismailcet.ECommerceBackend.repository.CategoryRepository;
import com.ismailcet.ECommerceBackend.repository.ProductRepository;
import com.ismailcet.ECommerceBackend.repository.SizeRepository;
import com.ismailcet.ECommerceBackend.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final JwtFilter jwtFilter;
    private final ProductDtoConverter productDtoConverter;
    private final SizeRepository sizeRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, JwtFilter jwtFilter, ProductDtoConverter productDtoConverter, SizeRepository sizeRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.jwtFilter = jwtFilter;
        this.productDtoConverter = productDtoConverter;
        this.sizeRepository = sizeRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductDto createProduct(CreateProductRequest createProductRequest) {
        try{
            if(jwtFilter.isAdmin()){
                Product product = Product.builder()
                        .name(createProductRequest.getName())
                        .photoUrl(createProductRequest.getPhotoUrl())
                        .color(createProductRequest.getColor())
                        .price(createProductRequest.getPrice())
                        .stock(createProductRequest.getStock())
                        .build();
                productRepository.save(product);
                return productDtoConverter.convert(product);
            }
            throw new AuthenticationException("Unauthenticated Access ! ");
        }catch (Exception ex){
            throw ex;
        }
    }

    public List<GetAllProductsResponse> getAllProduct() {
        try{
            List<GetAllProductsResponse> products =
                        productRepository.findAll().stream()
                                .map(p->new GetAllProductsResponse(
                                        p.getId(),
                                        p.getName(),
                                        p.getColor(),
                                        p.getPrice(),
                                        p.getPhotoUrl(),
                                        p.getStock(),
                                        p.getSizesProduct(),
                                        p.getCategoriesProduct()))
                                .collect(Collectors.toList());
            return products;
        }catch (Exception ex){
            throw ex;
        }
    }

    public ProductDto addSizeToProduct(Integer productId, Integer sizeId) {
        try{
            if(jwtFilter.isAdmin()){
                Set<Size> sizes = new HashSet<>();

                Size size = sizeRepository.findById(sizeId).get();
                Product product = productRepository.findById(productId).get();

                sizes = product.getSizesProduct();
                sizes.add(size);
                product.setSizesProduct(sizes);
                productRepository.save(product);
                return productDtoConverter.convert(product);
            }
            throw new AuthenticationException("Unauthenticated Access ! ");
        }catch (Exception ex){
            throw ex;
        }
    }

    public ProductDto addCategoryToProduct(Integer productId, Integer categoryId) {
        try{
            if(jwtFilter.isAdmin()){
                Set<Category> categories = null;
                Category category = categoryRepository.findById(categoryId).get();

                Product product = productRepository.findById(productId).get();
                categories = product.getCategoriesProduct();
                categories.add(category);
                product.setCategoriesProduct(categories);
                productRepository.save(product);
                return productDtoConverter.convert(product);
            }
            throw new AuthenticationException("Unauthenticated Access ! ");
        }catch (Exception ex){
           throw ex;
        }
    }

    public ProductDto updateProductByProductId(Integer id, UpdateProductRequest updateProductRequest) {
        try{
            if(jwtFilter.isAdmin()){
                Product product =
                        productRepository.findById(id).get();
                if(!Objects.isNull(product)){
                    product.setName(updateProductRequest.getName());
                    product.setPrice(updateProductRequest.getPrice());
                    product.setColor(updateProductRequest.getColor());
                    product.setPhotoUrl(updateProductRequest.getPhotoUrl());
                    product.setStock(updateProductRequest.getStock());
                    productRepository.save(product);
                    return productDtoConverter.convert(product);
                }
                throw new ProductNotFoundException("Product Id does not exist");
            }
            throw new AuthenticationException("Unauthenticated Access ! ");
        }catch (Exception ex){
            throw ex;
        }
    }

    public String deleteProductByProductId(Integer id) {
        try{
            if(jwtFilter.isAdmin()){
                Product product =
                        productRepository.findById(id).get();
                if(!Objects.isNull(product)){
                    productRepository.deleteById(id);
                    return "Product Successfully Deleted";
                }
                throw new ProductNotFoundException("Product Id does not exist !");
            }
            throw new AuthenticationException("Unauthorized Access ! ");
        }catch (Exception ex){
           throw ex;
        }
    }

    public GetProductResponse getProductByProductId(Integer id) {
        try{
            Optional<Product> product =
                    productRepository.findById(id);
            if(product.isPresent()){
                GetProductResponse p = GetProductResponse.builder()
                        .id(product.get().getId())
                        .name(product.get().getName())
                        .price(product.get().getPrice())
                        .color(product.get().getColor())
                        .photoUrl(product.get().getPhotoUrl())
                        .stock(product.get().getStock())
                        .categoriesProduct(product.get().getCategoriesProduct())
                        .sizesProduct(product.get().getSizesProduct())
                        .build();
                return p;
            }
            throw new ProductNotFoundException("Product Id does not exist !");
        }catch (Exception ex){
            throw ex;
        }
    }
}
