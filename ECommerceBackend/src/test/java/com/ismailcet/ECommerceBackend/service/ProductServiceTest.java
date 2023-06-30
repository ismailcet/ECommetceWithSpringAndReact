package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.dto.ProductDto;
import com.ismailcet.ECommerceBackend.dto.converter.ProductDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreateProductRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateProductRequest;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;

import java.util.*;

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
    @Test
    public void testCreateProduct_whenUserRoleIsNotAdmin_shouldCreateProductFailedAndThrowAuthenticationException(){
        CreateProductRequest given = CreateProductRequest.builder()
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .build();

        AuthenticationException excepted =
                new AuthenticationException("Unauthenticated Access ! ");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> productService.createProduct(given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
    }
    @Test
    public void testGetAllProduct_whenHaveToken_shouldReturnListOfProductDto(){
        Product product1 = Product.builder()
                .id(1)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .build();
        Product product2 = Product.builder()
                .id(2)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .build();
        List<Product> productList =
                new ArrayList<>(List.of(product1, product2));

        ProductDto productDto1 = ProductDto.builder()
                .id(1)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .build();
        ProductDto productDto2 = ProductDto.builder()
                .id(2)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .build();
        List<ProductDto> excepted =
                new ArrayList<>(List.of(productDto1, productDto2));

        when(productRepository.findAll()).thenReturn(productList);
        when(productDtoConverter.convert(product1)).thenReturn(productDto1);
        when(productDtoConverter.convert(product2)).thenReturn(productDto2);

        List<ProductDto> actual =
                productService.getAllProduct();

        assertEquals(excepted, actual);
        assertTrue(excepted.size() == actual.size() && excepted.containsAll(actual));

        verify(productRepository).findAll();
        verify(productDtoConverter, new Times(2)).convert(any(Product.class));

    }
    @Test
    public void testAddSizeToProduct_whenUserRoleIsAdminAndSizeIdIsExistAndProductIdIsExist_shouldSizeAddedSuccessfullyAndReturnProductDto(){
        Integer sizeId = 1;
        Size size =Size.builder()
                .id(1)
                .name("L")
                .build();

        Set<Size> sizes =
                new HashSet<>();

        Integer productId = 1;
        Product product = Product.builder()
                .id(1)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .sizesProduct(new HashSet<>())
                .build();

        sizes.add(size);

        Product result = Product.builder()
                .id(1)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .sizesProduct(sizes)
                .build();

        ProductDto excepted = ProductDto.builder()
                .id(1)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .sizesProduct(sizes)
                .build();

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sizeRepository.findById(any(Integer.class))).thenReturn(Optional.of(size));
        when(productRepository.findById(any(Integer.class))).thenReturn(Optional.of(product));
        when(productRepository.save(result)).thenReturn(result);
        when(productDtoConverter.convert(result)).thenReturn(excepted);


        ProductDto actual =
                productService.addSizeToProduct(productId,sizeId);

        assertEquals(excepted, actual);
        assertEquals(excepted.getSizesProduct().size(), actual.getSizesProduct().size());
        assertEquals(sizes, actual.getSizesProduct());

        verify(jwtFilter).isAdmin();
        verify(sizeRepository).findById(any(Integer.class));
        verify(productRepository).findById(any(Integer.class));
        verify(productRepository).save(any(Product.class));
        verify(productDtoConverter).convert(any(Product.class));

    }
    @Test
    public void testAddSizeToProduct_whenUserRoleIsNotAdmin_shouldThrowAuthenticationException(){
        Integer sizeId = 1;
        Integer productId = 1;
        AuthenticationException excepted =
                new AuthenticationException("Unauthenticated Access ! ");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()->productService.addSizeToProduct(productId, sizeId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();

    }
    @Test
    public void testAddSizeToProduct_whenUserRoleIsAdminAndSizeIdIsNotExist_shouldThrowSizeNotFoundException(){
        Integer sizeId = 1;
        Integer productId = 1;

        SizeNotFoundException excepted =
                new SizeNotFoundException("Size Not Found ! ");
        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sizeRepository.findById(sizeId)).thenReturn(Optional.empty());

        SizeNotFoundException actual = assertThrows(SizeNotFoundException.class,
                ()-> productService.addSizeToProduct(sizeId, productId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
        verify(sizeRepository).findById(any(Integer.class));
    }
    @Test
    public void testAddSizeToProduct_whenUserRoleIsAdminAndSizeIdIsExistAndProductIdIsNotExist_shouldThrowProductNotFoundException(){
        Integer sizeId = 1;

        Size size =Size.builder()
                .id(1)
                .name("L")
                .build();
        Integer productId = 1;

        ProductNotFoundException excepted =
                new ProductNotFoundException("Product Not Found ! ");

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sizeRepository.findById(any(Integer.class))).thenReturn(Optional.of(size));
        when(productRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        ProductNotFoundException actual = assertThrows(ProductNotFoundException.class,
                ()->productService.addSizeToProduct(sizeId, productId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
        verify(sizeRepository).findById(any(Integer.class));
        verify(productRepository).findById(any(Integer.class));


    }
    @Test
    public void testAddCategoryToProduct_whenUserRoleIsAdminAndCategoryIdIsExistAndProductIdIsExist_shouldCategoryAddedSuccessfullyAndReturnProductDto(){
        Integer categoryId = 1;
        Category category =Category.builder()
                .id(1)
                .name("test-category")
                .build();

        Set<Category> categories =
                new HashSet<>();

        Integer productId = 1;
        Product product = Product.builder()
                .id(1)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .categoriesProduct(new HashSet<>())
                .build();

        categories.add(category);

        Product result = Product.builder()
                .id(1)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .categoriesProduct(categories)
                .build();

        ProductDto excepted = ProductDto.builder()
                .id(1)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .categoriesProduct(categories)
                .build();

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));
        when(productRepository.findById(any(Integer.class))).thenReturn(Optional.of(product));
        when(productRepository.save(result)).thenReturn(result);
        when(productDtoConverter.convert(result)).thenReturn(excepted);


        ProductDto actual =
                productService.addCategoryToProduct(productId,categoryId);

        assertEquals(excepted, actual);
        assertEquals(excepted.getCategoriesProduct().size(), actual.getCategoriesProduct().size());
        assertEquals(categories, actual.getCategoriesProduct());

        verify(jwtFilter).isAdmin();
        verify(categoryRepository).findById(any(Integer.class));
        verify(productRepository).findById(any(Integer.class));
        verify(productRepository).save(any(Product.class));
        verify(productDtoConverter).convert(any(Product.class));

    }
    @Test
    public void testAddCategoryToProduct_whenUserRoleIsNotAdmin_shouldThrowAuthenticationException(){
        Integer categoryId = 1;
        Integer productId = 1;
        AuthenticationException excepted =
                new AuthenticationException("Unauthenticated Access ! ");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()->productService.addCategoryToProduct(productId, categoryId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();

    }
    @Test
    public void testAddCategoryToProduct_whenUserRoleIsAdminAndCategoryIdIsNotExist_shouldThrowCategoryNotFoundException(){
        Integer categoryId = 1;
        Integer productId = 1;

        CategoryNotFoundException excepted =
                new CategoryNotFoundException("Category Not Found ! ");
        when(jwtFilter.isAdmin()).thenReturn(true);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        CategoryNotFoundException actual = assertThrows(CategoryNotFoundException.class,
                ()-> productService.addCategoryToProduct(categoryId, productId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
        verify(categoryRepository).findById(any(Integer.class));
    }
    @Test
    public void testAddCategoryToProduct_whenUserRoleIsAdminAndCategoryIdIsExistAndProductIdNotExist_shouldThrowProductNotFoundException(){
        Integer categoryId = 1;
        Category category =Category.builder()
                .id(1)
                .name("test-category")
                .build();

        Integer productId = 1;

        ProductNotFoundException excepted =
                new ProductNotFoundException("Product Not Found ! ");

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(categoryRepository.findById(any(Integer.class))).thenReturn(Optional.of(category));
        when(productRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        ProductNotFoundException actual = assertThrows(ProductNotFoundException.class,
                ()->productService.addCategoryToProduct(categoryId, productId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
        verify(categoryRepository).findById(any(Integer.class));
        verify(productRepository).findById(any(Integer.class));


    }
    @Test
    public void testUpdateProductByProductId_whenUserRoleIsAdminAndProductIdIsExist_shouldSuccessfullyUpdatedAndReturnProductDto(){
        Integer productId = 1;
        UpdateProductRequest given = UpdateProductRequest.builder()
                .name("test-name-upd")
                .price(15.5)
                .color("test-color-upd")
                .photoUrl("test-url-upd")
                .stock(15)
                .build();

        Product product = Product.builder()
                .name("test-name")
                .price(15.5)
                .color("test-color")
                .photoUrl("test-url")
                .stock(15)
                .build();

        Product result = Product.builder()
                .name("test-name-upd")
                .price(15.5)
                .color("test-color-upd")
                .photoUrl("test-url-upd")
                .stock(15)
                .build();

        ProductDto excepted = ProductDto.builder()
                .name("test-name-upd")
                .price(15.5)
                .color("test-color-upd")
                .photoUrl("test-url-upd")
                .stock(15)
                .build();

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(result)).thenReturn(result);
        when(productDtoConverter.convert(result)).thenReturn(excepted);

        ProductDto actual =
                productService.updateProductByProductId(productId, given);

        assertEquals(excepted, actual);
        assertEquals(given.getName(), actual.getName());
        assertEquals(given.getColor(), actual.getColor());

        verify(jwtFilter).isAdmin();
        verify(productRepository).findById(any(Integer.class));
        verify(productRepository).save(any(Product.class));
        verify(productDtoConverter).convert(any(Product.class));
    }
    @Test
    public void testUpdateProductByProductId_whenUserRoleIsNotAdmin_shouldThrowsAuthenticationException(){
        Integer productId = 1;
        UpdateProductRequest given = UpdateProductRequest.builder()
                .name("test-name-upd")
                .price(15.5)
                .color("test-color-upd")
                .photoUrl("test-url-upd")
                .stock(15)
                .build();
        AuthenticationException excepted =
                new AuthenticationException("Unauthenticated Access ! ");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> productService.updateProductByProductId(productId, given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());


        verify(jwtFilter).isAdmin();
    }
    @Test
    public void testUpdateProductByProductId_whenUserRoleIsAdminAndProductIdIsNotExist_shouldThrowProductNotFoundException(){
        Integer productId = 1;
        UpdateProductRequest given = UpdateProductRequest.builder()
                .name("test-name-upd")
                .price(15.5)
                .color("test-color-upd")
                .photoUrl("test-url-upd")
                .stock(15)
                .build();
        
        ProductNotFoundException excepted = 
                new ProductNotFoundException("Product Id does not exist");
        
        when(jwtFilter.isAdmin()).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException actual = assertThrows(ProductNotFoundException.class,
                ()->productService.updateProductByProductId(productId, given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());


        verify(jwtFilter).isAdmin();
        verify(productRepository).findById(any(Integer.class));
    }
    @Test
    public void testDeleteProductByProductId_whenUserRoleIsAdminAndProductIdIsExist_shouldDeleteSuccessfullyAndReturnString(){
        Integer productId = 1;
        Product product = Product.builder()
                .name("test-name")
                .price(15.5)
                .color("test-color")
                .photoUrl("test-url")
                .stock(15)
                .build();

        String excepted = "Product Successfully Deleted";

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(productRepository.findById(any(Integer.class))).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(any(Integer.class));

        String actual =
                productService.deleteProductByProductId(productId);

        assertEquals(excepted, actual);

        verify(jwtFilter).isAdmin();
        verify(productRepository).findById(any(Integer.class));
        verify(productRepository).deleteById(any(Integer.class));
    }
    @Test
    public void testDeleteProductByProductId_whenUserRoleIsNotAdmin_shouldThrowsAuthenticationException(){
        Integer productId = 1;
        AuthenticationException excepted =
                new AuthenticationException("Unauthorized Access ! ");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> productService.deleteProductByProductId(productId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
    }
    @Test
    public void testDeleteProductByProductId_whenUserRoleIsAdminAndProductIdIsNotExist_shouldThrowProductNotFoundException(){
        Integer productId = 1;
        ProductNotFoundException excepted =
                new ProductNotFoundException("Product Id does not exist");

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException actual = assertThrows(ProductNotFoundException.class,
                ()->productService.deleteProductByProductId(productId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());


        verify(jwtFilter).isAdmin();
        verify(productRepository).findById(any(Integer.class));
    }
    @Test
    public void testGetProductByProductId_whenProductIdIsExist_shouldReturnProductDto(){
        Integer productId = 1;
        Product product = Product.builder()
                .name("test-name")
                .price(15.5)
                .color("test-color")
                .photoUrl("test-url")
                .stock(15)
                .build();

        ProductDto excepted = ProductDto.builder()
                .name("test-name")
                .price(15.5)
                .color("test-color")
                .photoUrl("test-url")
                .stock(15)
                .build();
        when(productRepository.findById(any(Integer.class))).thenReturn(Optional.of(product));
        when(productDtoConverter.convert(any(Product.class))).thenReturn(excepted);

        ProductDto actual =
                productService.getProductByProductId(productId);
        assertEquals(excepted, actual);
        assertEquals(product.getName(), actual.getName());

        verify(productRepository).findById(any(Integer.class));
        verify(productDtoConverter).convert(any(Product.class));
    }
    @Test
    public void testGetProductByProductId_whenProductIdIsNotExist_shouldThrowProductNotFoundException(){
        Integer productId = 1;
        ProductNotFoundException excepted =
                new ProductNotFoundException("Product Not Found ! ");

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException actual = assertThrows(ProductNotFoundException.class,
                ()->productService.getProductByProductId(productId));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());


        verify(productRepository).findById(any(Integer.class));
    }
}