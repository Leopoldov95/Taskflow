package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.user.CreateUserRequest;
import com.taskflow.taskflow.dto.user.UpdateUserPasswordRequest;
import com.taskflow.taskflow.dto.user.UpdateUserRequest;
import com.taskflow.taskflow.dto.user.UserResponse;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    // expose "/users" and get a list of users
    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return userService.findAll();
    }

    // Get a single user by id
    @GetMapping("/users/{userId}")
    public UserResponse getUser(@PathVariable int userId) {
        UserResponse theUser = userService.findById(userId);

        if (theUser == null) {
            throw new RuntimeException("User not found: " + userId);
        }

        return theUser;
    }

    // Create a new User
    @PostMapping("/users")
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        UserResponse dbUser = userService.save(request);

        return dbUser;
    }

    // Update (PATCH) an existing user field
    @PatchMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable int userId,
            @RequestBody UpdateUserRequest request) {
        UserResponse dbUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(dbUser);
    }

    // Update User password
    @PatchMapping("/users/{userId}/password")
    public ResponseEntity<UserResponse> updateUserPassword(
            @PathVariable int userId,
            @RequestBody UpdateUserPasswordRequest request
    ) {
        userService.updateUserPassword(userId, request);
        return ResponseEntity.noContent().build();
    }

    // Delete User by id
    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable int userId) {
        UserResponse tempUser = userService.findById(userId);

          // throw exception if null

          if (tempUser == null) {
              throw new RuntimeException("User id not found - " + userId);
          }

          userService.deleteById(userId);

          return "Deleted user id - " + userId;
    }
}