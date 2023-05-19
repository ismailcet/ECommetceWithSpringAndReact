package com.ismailcet.ECommerceBackend.repository;

import com.ismailcet.ECommerceBackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
