package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.dto.request.CreateCategoryRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateCategoryRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateSizeRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllCategoriesResponse;
import com.ismailcet.ECommerceBackend.dto.response.GetAllSizesResponse;
import com.ismailcet.ECommerceBackend.entity.Category;
import com.ismailcet.ECommerceBackend.entity.Size;
import com.ismailcet.ECommerceBackend.exception.AuthenticationException;
import com.ismailcet.ECommerceBackend.exception.CategoryNotFoundException;
import com.ismailcet.ECommerceBackend.exception.SizeNotFoundException;
import com.ismailcet.ECommerceBackend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    private CategoryRepository categoryRepository;
    private JwtFilter jwtFilter;
    private CategoryService categoryService;

    @BeforeEach
    public void setUp(){
        categoryRepository = mock(CategoryRepository.class);
        jwtFilter = mock(JwtFilter.class);
        categoryService =
                new CategoryService(categoryRepository, jwtFilter);
    }

    @Test
    public void testCreateCategory_whenUserRoleIsAdminAndCategoryNameIsNotExist_shouldCreateSuccessfullyAndReturnString(){
        CreateCategoryRequest given =
                new CreateCategoryRequest("test-category");

        Category category = Category.builder()
                .id(1)
                .name("test-name")
                .build();
        String excepted = "Category Successfully added ! ";

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(categoryRepository.findByName(any(String.class))).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        String actual =
                categoryService.createCategory(given);

        assertEquals(excepted, actual);

        verify(jwtFilter).isAdmin();
        verify(categoryRepository).findByName(any(String.class));
        verify(categoryRepository).save(any(Category.class));
    }
    @Test
    public void testCreateCategory_whenUserRoleIsNotAdmin_shouldThrowsAuthenticationException(){
        CreateCategoryRequest given =
                new CreateCategoryRequest("test-category");

        AuthenticationException excepted =
                new AuthenticationException("Unauthenticated Access ! ");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> categoryService.createCategory(given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
    }
    @Test
    public void testCreateCategory_whenUserRoleIsAdminAndCategoryNameIsExist_shouldThrowsCategoryIsNotFoundException(){
        CreateCategoryRequest given =
                new CreateCategoryRequest("test-category");

        Category category = Category.builder()
                .id(1)
                .name("test-name")
                .build();
        CategoryNotFoundException excepted =
                new CategoryNotFoundException("Category Name already exist ! ");

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(categoryRepository.findByName(any(String.class))).thenReturn(category);

        CategoryNotFoundException actual = assertThrows(CategoryNotFoundException.class,
                ()->categoryService.createCategory(given));
        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
        verify(categoryRepository).findByName(any(String.class));


    }
    @Test
    public void testUpdateCategoryByCategoryId_whenUserRoleIsAdminAndCategoryIdIsExist_shouldUpdatedSuccessfullyAndReturnString(){
        Integer sizeId = 1;

        UpdateCategoryRequest given =
                new UpdateCategoryRequest("test-size-upd");

        Category category = Category.builder()
                .id(1)
                .name("test-category")
                .build();
        Category updated = Category.builder()
                .id(1)
                .name("test-category-upd")
                .build();

        String excepted =
                "Category Successfully Updated ! ";

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updated);

        String actual =
                categoryService.updateCategoryByCategoryId(sizeId, given);

        assertEquals(excepted, actual);

        verify(jwtFilter).isAdmin();
        verify(categoryRepository).findById(any(Integer.class));
        verify(categoryRepository).save(any(Category.class));
    }
    @Test
    public void testUpdateCategoryByCategoryId_whenUserRoleIsAdminCategoryNameIsNotExist_shouldThrowsCategoryNotFoundException(){
        Integer categoryId = 1;
        UpdateCategoryRequest given =
                new UpdateCategoryRequest("test-category");

        CategoryNotFoundException excepted =
                new CategoryNotFoundException("Category Id does not exist ! ");

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        CategoryNotFoundException actual = assertThrows(CategoryNotFoundException.class,
                ()-> categoryService.updateCategoryByCategoryId(categoryId,given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
        verify(categoryRepository).findById(any(Integer.class));
    }
    @Test
    public void testUpdateCategoryByCategoryId_whenUserRoleIsNotAdmin_shouldThrowsAuthenticationNotFoundException(){
        Integer categoryId = 1;
        UpdateCategoryRequest given =
                new UpdateCategoryRequest("test-category");

        AuthenticationException excepted =
                new AuthenticationException("Unauthenticated Access ! ");

        when(jwtFilter.isAdmin()).thenReturn(false);


        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> categoryService.updateCategoryByCategoryId(categoryId,given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();

    }
    @Test
    public void testDeleteCategoryByCategoryId_whenUserRoleIsAdminAndCategoryIdIsExist_shouldReturnString(){
        Integer categoryId = 1;

        Category category = Category.builder()
                .id(1)
                .name("test-category")
                .build();

        String excepted =
                "Category Successfully Deleted ! ";

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(any(Integer.class));

        String actual =
                categoryService.deleteCategoryByCategoryId(categoryId);

        assertEquals(excepted, actual);

        verify(jwtFilter).isAdmin();
        verify(categoryRepository).findById(any(Integer.class));
        verify(categoryRepository).deleteById(any(Integer.class));
    }
    @Test
    public void testDeleteCategoryByCategoryId_whenUserRoleIsNotAdmin_shouldThrowsAuthenticationException(){
        Integer categoryId = 1;

        AuthenticationException excepted =
                new AuthenticationException("Unauthenticated Access ! ");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> categoryService.deleteCategoryByCategoryId(categoryId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
    }
    @Test
    public void testDeleteCategoryByCategoryId_whenUserRoleIsAdminAndCategoryIdIsNotExist_shouldThrowsCategoryNotFoundException(){
        Integer categoryId = 1;

        CategoryNotFoundException excepted =
                new CategoryNotFoundException("Category Id does not exist ! ");

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        CategoryNotFoundException actual = assertThrows(CategoryNotFoundException.class,
                ()-> categoryService.deleteCategoryByCategoryId(categoryId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
        verify(categoryRepository).findById(any(Integer.class));
    }
    @Test
    public void testGetAllCategories_whenUserRoleIsAdmin_shouldReturnListTypeOfGetAllCategoriesResponse(){
        Category cat1 = Category.builder()
                .id(1)
                .name("test-size")
                .build();
        Category cat2 = Category.builder()
                .id(2)
                .name("test-size")
                .build();

        List<Category> categories =
                Arrays.asList(cat1, cat2);

        GetAllCategoriesResponse c1 =
                new GetAllCategoriesResponse(1, "test-size");

        GetAllCategoriesResponse c2 =
                new GetAllCategoriesResponse(2, "test-size");

        List<GetAllCategoriesResponse> excepted = Arrays.asList(c1, c2);

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(categoryRepository.findAll()).thenReturn(categories);


        List<GetAllCategoriesResponse> actual =
                categoryService.getAllCategories();

        assertEquals(excepted.size(), actual.size());
        assertEquals(excepted.get(0),actual.get(0));

        verify(jwtFilter).isAdmin();
        verify(categoryRepository).findAll();

    }
    @Test
    public void testGetAllCategories_whenUserRoleIsNotAdmin_shouldThrowAuthenticationException(){
        AuthenticationException excepted =
                new AuthenticationException("Unauthenticated Access ! ");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> categoryService.getAllCategories());

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
    }
}
