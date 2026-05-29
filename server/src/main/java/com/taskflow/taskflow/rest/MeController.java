package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.auth.AuthResponse;
import com.taskflow.taskflow.dto.user.UpdateUserPasswordRequest;
import com.taskflow.taskflow.dto.user.UpdateUserRequest;
import com.taskflow.taskflow.dto.user.UpdateUserResponse;
import com.taskflow.taskflow.dto.user.UserResponse;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    private final UserService userService;

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

    @Autowired
    public MeController(UserService userService) {
        this.userService = userService;
    }

    // GET - get own profile
    @GetMapping
    public ResponseEntity<UserResponse> getMe() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(mapToResponse(userService.findById(currentUser.getId())));
    }

    // PATCH - update own name/email
    @PatchMapping
    public ResponseEntity<UpdateUserResponse> updateMe(@Valid @RequestBody UpdateUserRequest request){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.updateUser(currentUser.getId(), request));
    }

    // PATCH - update own password
    @PatchMapping("/password")
    public ResponseEntity<AuthResponse> updateMyPassword(@Valid @RequestBody UpdateUserPasswordRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuthResponse response = userService.updateUserPassword(currentUser.getId(), request);
        // this return type is needed to return a successful HTTP response without a body
        return ResponseEntity.ok(response);
    }
}
