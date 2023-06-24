package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.dto.ProductDto;
import com.ismailcet.ECommerceBackend.dto.converter.ProductDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreateProductRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateProductRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllProductsResponse;
import com.ismailcet.ECommerceBackend.dto.response.GetProductResponse;
import com.ismailcet.ECommerceBackend.entity.Category;
import com.ismailcet.ECommerceBackend.entity.ProductImage;
import com.ismailcet.ECommerceBackend.entity.Product;
import com.ismailcet.ECommerceBackend.entity.Size;
import com.ismailcet.ECommerceBackend.exception.AuthenticationException;
import com.ismailcet.ECommerceBackend.exception.ProductNotFoundException;
import com.ismailcet.ECommerceBackend.repository.CategoryRepository;
import com.ismailcet.ECommerceBackend.repository.ProductRepository;
import com.ismailcet.ECommerceBackend.repository.SizeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    private final String FOLDER_PATH = "/Users/ismailcetin/Desktop/Full Stack/Ecommerce/ECommetceWithSpringAndReact/images/";

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

    public List<ProductDto> getAllProduct() {
        try{
            List<ProductDto> products =
                        productRepository.findAll().stream()
                                .map(productDtoConverter::convert)
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

    public ProductDto getProductByProductId(Integer id) {
        try{
            Product product = productRepository.findById(id)
                    .orElseThrow(() ->new ProductNotFoundException("Product Not Found ! "));

            return productDtoConverter.convert(product);

        }catch (Exception ex){
            throw ex;
        }
    }

    public String addImagesToProduct(Integer id, MultipartFile file) throws IOException {
        String pathFile = FOLDER_PATH + file.getOriginalFilename();

        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException("Product Not Found ! "));

        ProductImage productImage = ProductImage.builder()
                .file(pathFile)
                .product(product)
                .build();

        file.transferTo(new File(pathFile));

        List<ProductImage> productImages = product.getProductImageList();
        productImages.add(productImage);
        product.setProductImageList(productImages);

        productRepository.save(product);


        return "Success " + file.getOriginalFilename();
    }

}
