package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.auth.AuthResponse;
import com.taskflow.taskflow.dto.user.UpdateUserPasswordRequest;
import com.taskflow.taskflow.dto.user.UpdateUserRequest;
import com.taskflow.taskflow.dto.user.UpdateUserResponse;
import com.taskflow.taskflow.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(int id);

    UpdateUserResponse updateUser(int id, UpdateUserRequest request);

    // Let's use a void response. Although this is a PATCH request user password does not really need a response body
    AuthResponse updateUserPassword (int id, UpdateUserPasswordRequest request);

    void deleteById(int id);
}
