package com.ismailcet.ECommerceBackend.repository;

import com.ismailcet.ECommerceBackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByUser_Id(Integer id);
}
