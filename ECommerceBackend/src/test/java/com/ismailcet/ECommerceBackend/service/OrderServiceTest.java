package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.dto.OrderItemDtoResponse;
import com.ismailcet.ECommerceBackend.dto.ProductDto;
import com.ismailcet.ECommerceBackend.dto.UserDto;
import com.ismailcet.ECommerceBackend.dto.converter.OrderDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreateOrderRequest;
import com.ismailcet.ECommerceBackend.entity.CargoStatus;
import com.ismailcet.ECommerceBackend.entity.OrderItem;
import com.ismailcet.ECommerceBackend.entity.Product;
import com.ismailcet.ECommerceBackend.entity.User;
import com.ismailcet.ECommerceBackend.repository.OrderRepository;
import com.ismailcet.ECommerceBackend.repository.ProductRepository;
import com.ismailcet.ECommerceBackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderDtoConverter orderDtoConverter;
    private JwtFilter jwtFilter;
    private OrderService orderService;

    @BeforeEach
    public void setUp(){
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        orderDtoConverter = mock(OrderDtoConverter.class);
        jwtFilter = mock(JwtFilter.class);

        orderService = new OrderService(
                orderRepository,
                userRepository,
                productRepository,
                orderDtoConverter,
                jwtFilter);
    }

    @Test
    public void testCreateOrder_whenUserRoleIsExistAndUserIdIsExistAndProductIdIsExist_shouldReturnOrderDto(){
        User user = User.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .password("test-password")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .gender("test-gender")
                .role("test-role")
                .build();

        Product product = Product.builder()
                .id(1)
                .name("test-name")
                .photoUrl("test-photo")
                .color("test-color")
                .price(15.0)
                .stock(5)
                .build();

        ProductDto productDto = ProductDto.builder()
                .name("test-name")
                .price(15.0)
                .color("test-color")
                .photoUrl("test-url")
                .stock(5)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .id(1)
                .product(product)
                .quantity(1)
                .build();
        List<OrderItem> items =
                Arrays.asList(orderItem);

        OrderItemDtoResponse orderItemDtoResponse = OrderItemDtoResponse.builder()
                .product(productDto)
                .quantity(1)
                .build();

        List<OrderItemDtoResponse> orderItemDtoResponses =
                Arrays.asList(orderItemDtoResponse);

        CreateOrderRequest given = CreateOrderRequest.builder()
                .createdDate(new Date())
                .cargoStatus(CargoStatus.PENDING)
                .address("test-address")
                .amount(16.0)
                .userId()
                .orderItems()
                .build();
    }
}