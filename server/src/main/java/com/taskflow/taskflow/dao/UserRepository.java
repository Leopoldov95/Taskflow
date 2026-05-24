package com.taskflow.taskflow.dao;

import com.taskflow.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Needed in order to check is an email already exists, already works OOB
    Optional<User> findByEmail(String email);
}
