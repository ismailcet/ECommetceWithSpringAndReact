package com.ismailcet.ECommerceBackend.controller;

import com.ismailcet.ECommerceBackend.dto.PaymentDto;
import com.ismailcet.ECommerceBackend.dto.request.CreatePaymentRequest;
import com.ismailcet.ECommerceBackend.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public ResponseEntity<String> createPayment(@RequestBody CreatePaymentRequest createPaymentRequest) throws StripeException {
        return new ResponseEntity(
                paymentService.createPayment(createPaymentRequest),
                HttpStatus.CREATED
        );
    }
    @GetMapping("/all")
    public ResponseEntity<List<PaymentDto>> getAllPayments(){
        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }
}
