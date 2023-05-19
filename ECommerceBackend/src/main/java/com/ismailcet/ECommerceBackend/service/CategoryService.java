package com.ismailcet.ECommerceBackend.service;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.dto.request.CreateCategoryRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateCategoryRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllCategoriesResponse;
import com.ismailcet.ECommerceBackend.entity.Category;
import com.ismailcet.ECommerceBackend.repository.CategoryRepository;
import com.ismailcet.ECommerceBackend.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final JwtFilter jwtFilter;

    public CategoryService(CategoryRepository categoryRepository, JwtFilter jwtFilter) {
        this.categoryRepository = categoryRepository;
        this.jwtFilter = jwtFilter;
    }

    public ResponseEntity<String> createCategory(CreateCategoryRequest createCategoryRequest) {
        try{
            if(jwtFilter.isAdmin()){
                Category category =
                        categoryRepository.findByName(createCategoryRequest.getName());

                if(Objects.isNull(category)){
                    Category newCat =
                            Category.builder()
                                    .name(createCategoryRequest.getName())
                                    .build();
                    categoryRepository.save(newCat);
                    return SystemUtils.getResponseEntity("Category Successfully added ! ", HttpStatus.CREATED);
                }
                return SystemUtils.getResponseEntity("Category Name already exist ! ", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> updateCategoryByCategoryId(Integer id, UpdateCategoryRequest updateCategoryRequest) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<Category> category =
                        categoryRepository.findById(id);
                if(category.isPresent()){
                    category.get().setName(updateCategoryRequest.getName());
                    categoryRepository.save(category.get());
                    return SystemUtils.getResponseEntity("Category Successfully Updated ! ", HttpStatus.OK);
                }
                return SystemUtils.getResponseEntity("Category Id does not exist ! ", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> deleteCategoryByCategoryId(Integer id) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<Category> category =
                        categoryRepository.findById(id);
                if(category.isPresent()){
                    categoryRepository.deleteById(id);
                    return SystemUtils.getResponseEntity("Category Successfully Deleted ! ", HttpStatus.OK);
                }
                return SystemUtils.getResponseEntity("Category Id does not exist ! " , HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<GetAllCategoriesResponse>> getAllCategories() {
        try{
            if(jwtFilter.isAdmin()){
                List<GetAllCategoriesResponse> categories =
                        categoryRepository.findAll().stream()
                                .map(c->new GetAllCategoriesResponse(c.getId(),c.getName()))
                                .collect(Collectors.toList());

                return new ResponseEntity<>(categories, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
