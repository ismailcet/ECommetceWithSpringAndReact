package com.ismailcet.ECommerceBackend.controller;

import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.dto.ProductDto;
import com.ismailcet.ECommerceBackend.dto.request.CreateProductRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateProductRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllProductsResponse;
import com.ismailcet.ECommerceBackend.dto.response.GetProductResponse;
import com.ismailcet.ECommerceBackend.entity.Product;
import com.ismailcet.ECommerceBackend.entity.ProductImage;
import com.ismailcet.ECommerceBackend.service.ProductService;
import com.ismailcet.ECommerceBackend.utils.SystemUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

            return new ResponseEntity<>(
                    productService.createProduct(createProductRequest),
                    HttpStatus.CREATED
            );

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProductByProductId(@PathVariable("id") Integer id,@RequestBody UpdateProductRequest updateProductRequest){

            return ResponseEntity.ok(productService.updateProductByProductId(id, updateProductRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductByProductId(@PathVariable("id") Integer id){
        productService.deleteProductByProductId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductByProductId(@PathVariable("id") Integer id){
            return ResponseEntity.ok(productService.getProductByProductId(id));
    }

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProduct(){
        System.out.println("Girdi");
            return ResponseEntity.ok(productService.getAllProduct());
    }

    @PutMapping("/{productId}/size/{sizeId}")
    public ResponseEntity<ProductDto> addSizeToProduct(@PathVariable("productId") Integer productId, @PathVariable("sizeId") Integer sizeId){
            return ResponseEntity.ok(productService.addSizeToProduct(productId, sizeId));
    }

    @PutMapping("/{productId}/category/{categoryId}")
    public ResponseEntity<ProductDto> addCategoryToProduct(@PathVariable("productId") Integer productId, @PathVariable("categoryId")Integer categoryId){
            return ResponseEntity.ok(productService.addCategoryToProduct(productId, categoryId));
    }

    @PostMapping("/{productId}/images/add")
    public ResponseEntity<String> addImagesToProduct(@PathVariable("productId") Integer id, @RequestParam("images") MultipartFile file) throws IOException {
        return ResponseEntity.ok(
                productService.addImagesToProduct(id, file)
        );
    }
    @GetMapping("/{productId}/images/get")
    public ResponseEntity<List<ProductImage>> getImagesLink(@PathVariable("productId") Integer id){
        return ResponseEntity.ok(
          productService.getImagesLink(id)
        );
    }
}
