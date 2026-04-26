package com.taskflow.taskflow.service;

import com.taskflow.taskflow.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(int id);

    User findByEmail(String email);

    // User save(User user);
    // using DTO method to avoid insertion of unecessary fields like id
    User save(User user);

    void deleteById(int id);
}
