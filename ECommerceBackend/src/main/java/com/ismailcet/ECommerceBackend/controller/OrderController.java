package com.ismailcet.ECommerceBackend.controller;

import com.ismailcet.ECommerceBackend.dto.request.CreateOrderRequest;
import com.ismailcet.ECommerceBackend.entity.Order;
import com.ismailcet.ECommerceBackend.entity.Product;
import com.ismailcet.ECommerceBackend.exception.ProductNotFoundException;
import com.ismailcet.ECommerceBackend.exception.UserNotFoundException;
import com.ismailcet.ECommerceBackend.repository.OrderRepository;
import com.ismailcet.ECommerceBackend.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    public OrderController(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Order> createOrder(@RequestBody  createOrderRequest){


        System.out.println(productRepository.findById(7));
        throw new ProductNotFoundException("not");
    }
}
