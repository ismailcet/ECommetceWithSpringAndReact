package com.ismailcet.ECommerceBackend.repository;

import com.ismailcet.ECommerceBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

}
