package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.user.UserResponse;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private UserService userService;

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
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    // expose "/users" and get a list of users
    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return userService.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get a single user by id
    @GetMapping("/users/{userId}")
    public UserResponse getUser(@PathVariable int userId) {
        User theUser = userService.findById(userId);

        return mapToResponse(theUser);
    }

    // Create a new User
    //! Update - Do not need this as it is handled during registration
//    @PostMapping("/users")
//    public UserResponse createUser(@RequestBody CreateUserRequest request) {
//        UserResponse dbUser = userService.save(request);
//
//        return dbUser;
//    }

    // Deleted methods to updated user, that'll only be handled in the Authentication layer

    // Delete User by id
    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable int userId) {
        User tempUser = userService.findById(userId);

          // throw exception if null

          if (tempUser == null) {
              throw new RuntimeException("User id not found - " + userId);
          }

          userService.deleteById(userId);

          return "Deleted user id - " + userId;
    }
}