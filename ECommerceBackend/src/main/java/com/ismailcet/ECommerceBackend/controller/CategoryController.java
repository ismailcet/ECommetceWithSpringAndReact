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
        try{
            return categoryService.createCategory(createCategoryRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategoryByCategoryId(@PathVariable("id") Integer id, @RequestBody UpdateCategoryRequest updateCategoryRequest){
        try{
            return categoryService.updateCategoryByCategoryId(id, updateCategoryRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryByCategoryId(@PathVariable("id") Integer id){
        try{
            return categoryService.deleteCategoryByCategoryId(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping()
    public ResponseEntity<List<GetAllCategoriesResponse>> getAllCategories(){
        try{
            return categoryService.getAllCategories();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
