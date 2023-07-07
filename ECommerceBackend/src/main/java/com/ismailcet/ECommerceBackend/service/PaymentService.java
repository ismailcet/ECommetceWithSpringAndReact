package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.dto.PaymentDto;
import com.ismailcet.ECommerceBackend.dto.converter.PaymentDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreatePaymentRequest;
import com.ismailcet.ECommerceBackend.entity.Order;
import com.ismailcet.ECommerceBackend.entity.Payment;
import com.ismailcet.ECommerceBackend.entity.User;
import com.ismailcet.ECommerceBackend.exception.AuthenticationException;
import com.ismailcet.ECommerceBackend.exception.OrderNotFoundException;
import com.ismailcet.ECommerceBackend.repository.OrderRepository;
import com.ismailcet.ECommerceBackend.repository.PaymentRepository;
import com.ismailcet.ECommerceBackend.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Value("${stripe.apikey}")
    private String apiKey;
    private final PaymentRepository paymentRepository;
    private final JwtFilter jwtFilter;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentDtoConverter converter;

    public PaymentService(PaymentRepository paymentRepository, JwtFilter jwtFilter, UserRepository userRepository, OrderRepository orderRepository, PaymentDtoConverter converter) {

        this.paymentRepository = paymentRepository;
        this.jwtFilter = jwtFilter;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.converter = converter;
    }

    public String createPayment(CreatePaymentRequest createPaymentRequest) throws StripeException {
        try{
            Order order = orderRepository.findById(createPaymentRequest.getOrderId()).orElseThrow(() -> new OrderNotFoundException("Order Not Found ! "));
            User user = userRepository.findByEmail(jwtFilter.getCurrentUser());
            if(order.getUser().getEmail().equals(jwtFilter.getCurrentUser())){
                Payment payment = Payment.builder()
                        .price(createPaymentRequest.getPrice())
                        .paymentDate(new Date())
                        .order(order)
                        .user(user)
                        .build();
                paymentRepository.save(payment);

                Stripe.apiKey = apiKey;
                Map<String, Object> params = new HashMap<>();
                params.put("amount", (int) createPaymentRequest.getPrice());
                params.put("currency", "usd");
                params.put("source", "tok_visa");
                params.put("description", "Order Id: "+order.getId()+ "From :" + user.getName());
                Charge.create(params);
                return "Success";
            }else{
                throw new AuthenticationException("Invalid Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public List<PaymentDto> getAllPayments() {
        try{
            if(jwtFilter.isAdmin()){
                List<PaymentDto> result = paymentRepository.findAll()
                        .stream()
                        .map(e -> converter.convert(e))
                        .collect(Collectors.toList());

                return result;
            }else{
                throw new AuthenticationException("Invalid Access ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }
}
