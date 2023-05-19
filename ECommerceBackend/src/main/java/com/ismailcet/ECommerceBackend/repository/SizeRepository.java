package com.ismailcet.ECommerceBackend.repository;

import com.ismailcet.ECommerceBackend.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface SizeRepository extends JpaRepository<Size, Integer> {

    Size findByName(String name);
}
