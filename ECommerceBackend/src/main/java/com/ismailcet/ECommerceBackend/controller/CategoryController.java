package com.ismailcet.ECommerceBackend.controller;

import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.dto.request.CreateCategoryRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateCategoryRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllCategoriesResponse;
import com.ismailcet.ECommerceBackend.service.CategoryService;
import com.ismailcet.ECommerceBackend.utils.SystemUtils;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping()
    public ResponseEntity<String> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest){

            return new ResponseEntity<>(categoryService.createCategory(createCategoryRequest),
                    HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategoryByCategoryId(@PathVariable("id") Integer id, @RequestBody UpdateCategoryRequest updateCategoryRequest){

            return ResponseEntity.ok(categoryService.updateCategoryByCategoryId(id, updateCategoryRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryByCategoryId(@PathVariable("id") Integer id){
        categoryService.deleteCategoryByCategoryId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<GetAllCategoriesResponse>> getAllCategories(){
            return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
