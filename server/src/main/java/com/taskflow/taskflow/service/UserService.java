package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.user.CreateUserRequest;
import com.taskflow.taskflow.dto.user.UpdateUserPasswordRequest;
import com.taskflow.taskflow.dto.user.UpdateUserRequest;
import com.taskflow.taskflow.dto.user.UserResponse;
import com.taskflow.taskflow.entity.User;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();

    UserResponse findById(int id);

    User findByEmail(String email);

    // User save(User user);
    // using DTO method to avoid insertion of unecessary fields like id
    UserResponse save(CreateUserRequest request);

    UserResponse updateUser(int id, UpdateUserRequest request);

    // Let's use a void response. Although this is a PATCH request user password does not really need a response body
    void updateUserPassword (int id, UpdateUserPasswordRequest request);

    void deleteById(int id);
}
