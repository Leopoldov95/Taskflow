package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.RoleRepository;
import com.taskflow.taskflow.dao.UserRepository;
import com.taskflow.taskflow.dto.user.CreateUserRequest;
import com.taskflow.taskflow.dto.user.UpdateUserPasswordRequest;
import com.taskflow.taskflow.dto.user.UpdateUserRequest;
import com.taskflow.taskflow.dto.user.UserResponse;
import com.taskflow.taskflow.entity.Role;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.entity.enums.RoleType;
import com.taskflow.taskflow.exception.BadRequestException;
import com.taskflow.taskflow.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private RoleRepository roleRepository;

    // Required to enable password encryption and creation
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // method to ensure we are getting User response in correct format
    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isActive()
        );
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public UserResponse findById(int id) {
        Optional<User> user = userRepository.findById(id);

        User theUser = null;

        if (user.isPresent()) {
            theUser = user.get();
        } else {
            // User not found
            throw new RuntimeException("Did not find user with id: " + id);
        }
        return mapToResponse(theUser);
    }


    // Creates a new user and sets the role (default is ROLE_MEMBER)
    @Transactional
    @Override
    public UserResponse save(CreateUserRequest request) {
        // OLD method, switching to DTO

        // 🔐 encode password
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        // 💾 save user first
//        User savedUser = userRepository.save(user);
//
//        // 👤 assign default role
//        Role role = roleRepository.findByName(RoleType.ROLE_MEMBER.name());
//
//        savedUser.setRoles(Set.of(role));
//
//        return userRepository.save(savedUser);

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());


        // encode password here (IMPORTANT)
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // default role assignment
        Role role = roleRepository.findByName(RoleType.ROLE_MEMBER.name());
        user.setRoles(Set.of(role));

        return mapToResponse(userRepository.save(user));
    }

    // Update user fields
    @Override
    public UserResponse updateUser(int id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Did not find user with id: " + id));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());

        userRepository.save(user);
        return mapToResponse(user);
    }

    // Update User password
    @Override
    public void updateUserPassword(int id, UpdateUserPasswordRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Did not find user with id: " + id));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is not correct");
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
}