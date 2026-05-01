package com.taskflow.taskflow.dao;

import com.taskflow.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    //... Handles JPA database methods

    // Add custom logic to handle find by email
    // Needed in order to check is an email already exists, already works OOB
    Optional<User> findByEmail(String email);
}
