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
import com.ismailcet.ECommerceBackend.repository.CategoryRepository;
import com.ismailcet.ECommerceBackend.repository.ProductRepository;
import com.ismailcet.ECommerceBackend.repository.SizeRepository;
import com.ismailcet.ECommerceBackend.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

    public ResponseEntity<ProductDto> createProduct(CreateProductRequest createProductRequest) {
        try{
            if(jwtFilter.isAdmin()){
                Product product = Product.builder()
                        .name(createProductRequest.getName())
                        .photoUrl(createProductRequest.getPhotoUrl())
                        .color(createProductRequest.getColor())
                        .price(createProductRequest.getPrice())
                        .build();
                productRepository.save(product);
                return new ResponseEntity<>(productDtoConverter.convert(product), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<GetAllProductsResponse>> getAllProduct() {
        try{
            List<GetAllProductsResponse> products =
                        productRepository.findAll().stream()
                                .map(p->new GetAllProductsResponse(
                                        p.getId(),
                                        p.getName(),
                                        p.getColor(),
                                        p.getPrice(),
                                        p.getPhotoUrl(),
                                        p.getSizesProduct(),
                                        p.getCategoriesProduct()))
                                .collect(Collectors.toList());
            return new ResponseEntity<>(products, HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<ProductDto> addSizeToProduct(Integer productId, Integer sizeId) {
        try{
            if(jwtFilter.isAdmin()){
                Set<Size> sizes = null;
                Size size = sizeRepository.findById(sizeId).get();

                Product product = productRepository.findById(productId).get();
                sizes = product.getSizesProduct();
                sizes.add(size);
                product.setSizesProduct(sizes);
                productRepository.save(product);
                return new ResponseEntity<>(productDtoConverter.convert(product),HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<ProductDto> addCategoryToProduct(Integer productId, Integer categoryId) {
        try{
            if(jwtFilter.isAdmin()){
                Set<Category> categories = null;
                Category category = categoryRepository.findById(categoryId).get();

                Product product = productRepository.findById(productId).get();
                categories = product.getCategoriesProduct();
                categories.add(category);
                product.setCategoriesProduct(categories);
                productRepository.save(product);
                return new ResponseEntity<>(productDtoConverter.convert(product), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<ProductDto> updateProductByProductId(Integer id, UpdateProductRequest updateProductRequest) {
        try{
            if(jwtFilter.isAdmin()){
                Product product =
                        productRepository.findById(id).get();
                if(!Objects.isNull(product)){
                    product.setName(updateProductRequest.getName());
                    product.setPrice(updateProductRequest.getPrice());
                    product.setColor(updateProductRequest.getColor());
                    product.setPhotoUrl(updateProductRequest.getPhotoUrl());
                    productRepository.save(product);
                    return new ResponseEntity<>(productDtoConverter.convert(product), HttpStatus.OK);
                }
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> deleteProductByProductId(Integer id) {
        try{
            if(jwtFilter.isAdmin()){
                Product product =
                        productRepository.findById(id).get();
                if(!Objects.isNull(product)){
                    productRepository.deleteById(id);
                    return SystemUtils.getResponseEntity("Product Successfully Deleted" , HttpStatus.OK);
                }
                return SystemUtils.getResponseEntity("Product Id does not exist !",HttpStatus.BAD_REQUEST);
            }
            return SystemUtils.getResponseEntity("Unauthorized Access ! ",HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<GetProductResponse> getProductByProductId(Integer id) {
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
                        .categoriesProduct(product.get().getCategoriesProduct())
                        .sizesProduct(product.get().getSizesProduct())
                        .build();
                return new ResponseEntity<>(p, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
