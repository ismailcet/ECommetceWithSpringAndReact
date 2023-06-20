package com.ismailcet.ECommerceBackend.controller;

import com.ismailcet.ECommerceBackend.dto.OrderDto;
import com.ismailcet.ECommerceBackend.dto.request.CreateOrderRequest;
import com.ismailcet.ECommerceBackend.entity.Order;
import com.ismailcet.ECommerceBackend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/add")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest createOrderRequest){
        return new ResponseEntity<>(
                orderService.createOrder(createOrderRequest),
                HttpStatus.CREATED
        );
    }
    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }
}
