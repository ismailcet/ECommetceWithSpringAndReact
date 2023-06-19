package com.ismailcet.ECommerceBackend.repository;

import com.ismailcet.ECommerceBackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
