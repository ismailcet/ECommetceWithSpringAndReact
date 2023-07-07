package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.dto.*;
import com.ismailcet.ECommerceBackend.dto.converter.PaymentDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreatePaymentRequest;
import com.ismailcet.ECommerceBackend.entity.*;
import com.ismailcet.ECommerceBackend.repository.OrderRepository;
import com.ismailcet.ECommerceBackend.repository.PaymentRepository;
import com.ismailcet.ECommerceBackend.repository.UserRepository;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepository;
    private JwtFilter jwtFilter;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private PaymentDtoConverter paymentDtoConverter;
    private PaymentService paymentService;
    @BeforeEach
    public void setUp(){
        paymentRepository = mock(PaymentRepository.class);
        jwtFilter = mock(JwtFilter.class);
        userRepository = mock(UserRepository.class);
        orderRepository = mock(OrderRepository.class);
        paymentDtoConverter = mock(PaymentDtoConverter.class);

        paymentService = new PaymentService(paymentRepository,
                                jwtFilter,
                                userRepository,
                                orderRepository,
                                paymentDtoConverter);
    }


    @Test
    public void testGetAllPayments_whenUserRoleIsAdmin_shouldReturnListOfPaymentDTOType(){
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

        Order order = Order.builder()
                .id(1)
                .orderNumber("test-ordernumber")
                .cargoStatus(CargoStatus.PENDING)
                .address("test-address")
                .amount(15.5)
                .user(user)
                .orderItems(items)
                .createdDate(new Date())
                .build();

        OrderDto orderDto = OrderDto.builder()
                .createdDate(new Date())
                .cargoStatus(CargoStatus.PENDING)
                .address("test-address")
                .amount(15.5)
                .user(userDto)
                .orderItems(orderItemDtoResponses)
                .build();

        Payment payment1 = Payment.builder()
                .price(16.5)
                .paymentDate(new Date())
                .order(order)
                .user(user)
                .build();
        Payment payment2 = Payment.builder()
                .price(16.5)
                .paymentDate(new Date())
                .order(order)
                .user(user)
                .build();

        List<Payment> result =
                Arrays.asList(payment1, payment2);

        PaymentDto paymentDto1 = PaymentDto.builder()
                .price(16.5)
                .user(userDto)
                .order(orderDto)
                .build();

        PaymentDto paymentDto2 = PaymentDto.builder()
                .price(16.5)
                .user(userDto)
                .order(orderDto)
                .build();
        List<PaymentDto> excepted =
                Arrays.asList(paymentDto1, paymentDto2);

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(paymentRepository.findAll()).thenReturn(result);
        when(paymentDtoConverter.convert(any(Payment.class))).thenReturn(paymentDto1);
        when(paymentDtoConverter.convert(any(Payment.class))).thenReturn(paymentDto2);


        List<PaymentDto> actual =
                paymentService.getAllPayments();

        assertEquals(excepted.size(), actual.size());
        Assertions.assertIterableEquals(excepted, actual);

        verify(jwtFilter).isAdmin();
        verify(paymentRepository).findAll();
        verify(paymentDtoConverter, new Times(2)).convert(any(Payment.class));
    }

}