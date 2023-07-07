package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.dto.OrderDto;
import com.ismailcet.ECommerceBackend.dto.OrderItemDto;
import com.ismailcet.ECommerceBackend.dto.converter.OrderDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreateOrderRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateCargoStatusRequest;
import com.ismailcet.ECommerceBackend.entity.*;
import com.ismailcet.ECommerceBackend.exception.AuthenticationException;
import com.ismailcet.ECommerceBackend.exception.OrderNotFoundException;
import com.ismailcet.ECommerceBackend.exception.ProductNotFoundException;
import com.ismailcet.ECommerceBackend.exception.UserNotFoundException;
import com.ismailcet.ECommerceBackend.repository.OrderRepository;
import com.ismailcet.ECommerceBackend.repository.ProductRepository;
import com.ismailcet.ECommerceBackend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderDtoConverter converter;
    private final JwtFilter jwtFilter;

    public OrderService (OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderDtoConverter converter, JwtFilter jwtFilter) {
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
                order.setOrderNumber(generateOrderNumber(user.getId()));

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
    private String generateOrderNumber(Integer userId){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmssddMMyyyy");
        String currentDateTime = now.format(formatter);
        String randomDigits = generateNumberDigits(4);
        String combination = userId + "-" + currentDateTime + "-" + randomDigits;

        return combination;
    }

    private String generateNumberDigits(int i) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(i);

        for(int j = 0; j<i;j++){
            sb.append(random.nextInt(10));
        }
        return sb.toString();
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

    public List<OrderDto> getAllOrdersByUserId(Integer userId) {
        try{
            User user = userRepository
                    .findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found ! "));
            if(jwtFilter.getCurrentUser().equals(user.getEmail())){
                List<OrderDto> orders =
                        orderRepository.findAllByUser_Id(userId)
                                .stream()
                                .map(e -> converter.convert(e))
                                .collect(Collectors.toList());
                return orders;
            }else{
                throw new AuthenticationException("Invalid Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public OrderDto getOrderByOrderId(Integer id) {
        try{
            Order order = orderRepository
                    .findById(id)
                    .orElseThrow(() -> new OrderNotFoundException("Order Not Found Exception"));
            if(jwtFilter.isAdmin() || jwtFilter.getCurrentUser().equals(order.getUser().getEmail())){
                return converter.convert(order);
            }else{
                throw new AuthenticationException("Invalid Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public CargoStatus updateCargoStatusByOrderId(Integer orderId, UpdateCargoStatusRequest cargoStatus) {
        try{
            if(jwtFilter.isAdmin()){
                Order order = orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new OrderNotFoundException("Order Not Found ! "));

                order.setCargoStatus(cargoStatus.getCargoStatus());
                orderRepository.save(order);
                return order.getCargoStatus();
            }else{
                throw new AuthenticationException("Invalid Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public String cancelOrderByOrderId(Integer orderId) {
        try{
            Order order = orderRepository
                    .findById(orderId)
                    .orElseThrow(
                            () -> new OrderNotFoundException("Order Not Found !"));
            if(jwtFilter.getCurrentUser().equals(order.getUser().getEmail()) || jwtFilter.isAdmin()){
                if(order.getCargoStatus() != CargoStatus.IN_TRANSIT){
                    orderRepository.deleteById(orderId);
                    return "Order Successfully Canceled";
                }else{
                    throw new OrderNotFoundException("Your Order is on its way ! ");
                }
            }else{
                throw new AuthenticationException("Invalid Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }
}
