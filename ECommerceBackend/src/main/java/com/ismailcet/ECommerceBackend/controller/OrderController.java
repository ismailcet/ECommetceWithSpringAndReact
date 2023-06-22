package com.ismailcet.ECommerceBackend.controller;

import com.ismailcet.ECommerceBackend.dto.OrderDto;
import com.ismailcet.ECommerceBackend.dto.request.CreateOrderRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateCargoStatusRequest;
import com.ismailcet.ECommerceBackend.entity.CargoStatus;
import com.ismailcet.ECommerceBackend.entity.Order;
import com.ismailcet.ECommerceBackend.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
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
    @GetMapping("/{userId}/get")
    public ResponseEntity<List<OrderDto>> getAllOrdersByUserId(@PathVariable("userId") Integer userId){
        return ResponseEntity.ok(
                orderService.getAllOrdersByUserId(userId)
        );
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<OrderDto> getOrderByOrderId(@PathVariable("id") Integer id){
        return ResponseEntity.ok(
                orderService.getOrderByOrderId(id)
        );
    }
    @PutMapping("/change/cargo/{orderId}")
    public ResponseEntity<CargoStatus> updateCargoStatusByOrderId(@PathVariable("orderId") Integer orderId, @RequestBody UpdateCargoStatusRequest cargoStatus){
        System.out.println(cargoStatus);
        return ResponseEntity.ok(
                orderService.updateCargoStatusByOrderId(orderId, cargoStatus)
        );
    }
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrderByOrderId(@PathVariable("orderId") Integer orderId){
        return new ResponseEntity<>(
          orderService.cancelOrderByOrderId(orderId),
          HttpStatus.NO_CONTENT
        );
    }
}
