package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.user.UpdateUserPasswordRequest;
import com.taskflow.taskflow.dto.user.UpdateUserRequest;
import com.taskflow.taskflow.dto.user.UserResponse;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final UserService userService;

    @Autowired
    public MeController(UserService userService) {
        this.userService = userService;
    }

    // GET - get own profile
    @GetMapping
    public ResponseEntity<UserResponse> getMe() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.findById(currentUser.getId()));
    }

    // PATCH - update own name/email
    @PatchMapping
    public ResponseEntity<UserResponse> updateMe(@RequestBody UpdateUserRequest request){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.updateUser(currentUser.getId(), request));
    }

    // PATCH - update own password
    @PatchMapping("/password")
    public ResponseEntity<Void> updateMyPassword(@RequestBody UpdateUserPasswordRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updateUserPassword(currentUser.getId(), request);
        // this return type is needed to return a successful HTTP response without a body
        return ResponseEntity.noContent().build();
    }
}
