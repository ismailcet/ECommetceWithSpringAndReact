package com.ismailcet.ECommerceBackend.controller;

import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.dto.ProductDto;
import com.ismailcet.ECommerceBackend.dto.request.CreateProductRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateProductRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllProductsResponse;
import com.ismailcet.ECommerceBackend.dto.response.GetProductResponse;
import com.ismailcet.ECommerceBackend.entity.Product;
import com.ismailcet.ECommerceBackend.service.ProductService;
import com.ismailcet.ECommerceBackend.utils.SystemUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequest createProductRequest){
        try{
            return productService.createProduct(createProductRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProductByProductId(@PathVariable("id") Integer id,@RequestBody UpdateProductRequest updateProductRequest){
        try{
            return productService.updateProductByProductId(id, updateProductRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductByProductId(@PathVariable("id") Integer id){
        try{
            return productService.deleteProductByProductId(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProductResponse> getProductByProductId(@PathVariable("id") Integer id){
        try{
            return productService.getProductByProductId(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping()
    public ResponseEntity<List<GetAllProductsResponse>> getAllProduct(){
        try{
            return productService.getAllProduct();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{productId}/size/{sizeId}")
    public ResponseEntity<ProductDto> addSizeToProduct(@PathVariable("productId") Integer productId, @PathVariable("sizeId") Integer sizeId){
        try{
            return productService.addSizeToProduct(productId, sizeId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{productId}/category/{categoryId}")
    public ResponseEntity<ProductDto> addCategoryToProduct(@PathVariable("productId") Integer productId, @PathVariable("categoryId")Integer categoryId){
        try{
            return productService.addCategoryToProduct(productId, categoryId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
