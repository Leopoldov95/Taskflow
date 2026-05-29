package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.RoleRepository;
import com.taskflow.taskflow.dao.UserRepository;
import com.taskflow.taskflow.dto.auth.AuthResponse;
import com.taskflow.taskflow.dto.user.UpdateUserPasswordRequest;
import com.taskflow.taskflow.dto.user.UpdateUserRequest;
import com.taskflow.taskflow.dto.user.UpdateUserResponse;
import com.taskflow.taskflow.dto.user.UserResponse;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.exception.BadRequestException;
import com.taskflow.taskflow.exception.DuplicateResourceException;
import com.taskflow.taskflow.exception.ResourceNotFoundException;
import com.taskflow.taskflow.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private JwtService jwtService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Autowired
    private RoleRepository roleRepository;

    // Required to enable password encryption and creation
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int id) {
        Optional<User> user = userRepository.findById(id);

        User theUser = null;

        if (user.isPresent()) {
            theUser = user.get();
        } else {
            // User not found
            throw new ResourceNotFoundException("Did not find user with id: " + id);
        }
        return theUser;
    }


    // Update user fields
    @Override
    public UpdateUserResponse updateUser(int id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Did not find user with id: " + id));
        boolean isEmailUpdated = false;

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        // need to ensure email remains unique
        if (request.getEmail() != null) {
            // check that email not already in use
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException(
                        "Email already in use"
                );
            }
            user.setEmail(request.getEmail());
            isEmailUpdated = true;
        }

        userRepository.save(user);

        // create new UserResponse
        UserResponse userResponse = new UserResponse(
                user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(), user.isActive()
        );

        if (isEmailUpdated) {
            return new UpdateUserResponse(userResponse, jwtService.generateToken(user));
        }

        return new UpdateUserResponse(userResponse);
    }

    // Update User password
    @Override
    public AuthResponse updateUserPassword(int id, UpdateUserPasswordRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Did not find user with id: " + id));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is not correct");
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }


    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
}