package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.dto.OrderDto;
import com.ismailcet.ECommerceBackend.dto.OrderItemDto;
import com.ismailcet.ECommerceBackend.dto.converter.OrderDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreateOrderRequest;
import com.ismailcet.ECommerceBackend.entity.Order;
import com.ismailcet.ECommerceBackend.entity.OrderItem;
import com.ismailcet.ECommerceBackend.entity.Product;
import com.ismailcet.ECommerceBackend.entity.User;
import com.ismailcet.ECommerceBackend.exception.AuthenticationException;
import com.ismailcet.ECommerceBackend.exception.ProductNotFoundException;
import com.ismailcet.ECommerceBackend.exception.UserNotFoundException;
import com.ismailcet.ECommerceBackend.repository.OrderRepository;
import com.ismailcet.ECommerceBackend.repository.ProductRepository;
import com.ismailcet.ECommerceBackend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderDtoConverter converter;
    private final JwtFilter jwtFilter;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderDtoConverter converter, JwtFilter jwtFilter) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.converter = converter;
        this.jwtFilter = jwtFilter;
    }

    public OrderDto createOrder(CreateOrderRequest createOrderRequest) {
        try{
            if(jwtFilter.isUser() || jwtFilter.isAdmin()){
                Order order = new Order();
                order.setAddress(createOrderRequest.getAddress());
                order.setAmount(createOrderRequest.getAmount());
                order.setCargoStatus(createOrderRequest.getCargoStatus());
                order.setCreatedDate(new Date());

                User user = userRepository.findById(createOrderRequest.getUserId()).orElseThrow(() -> new UserNotFoundException("User Not Found ! "));
                order.setUser(user);

                List<OrderItem> orderItems = new ArrayList<>();
                for(OrderItemDto orderItemDto:createOrderRequest.getOrderItems()){
                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(orderItemDto.getQuantity());
                    orderItem.setOrder(order);

                    Product product = productRepository.findById(orderItemDto.getProductId()).orElseThrow(() -> new ProductNotFoundException("Product Not Found ! "));
                    orderItem.setProduct(product);
                    orderItems.add(orderItem);
                }

                order.setOrderItems(orderItems);
                orderRepository.save(order);
                return converter.convert(order);
            }else{
                throw new AuthenticationException("Invalid Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public List<OrderDto> getAllOrders() {
        try{
            if(jwtFilter.isAdmin()){
                List<OrderDto> orderList =
                        orderRepository.findAll().stream().map(e -> converter.convert(e)).collect(Collectors.toList());

                return orderList;
            }else{
                throw new AuthenticationException("Invalid Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }
}
