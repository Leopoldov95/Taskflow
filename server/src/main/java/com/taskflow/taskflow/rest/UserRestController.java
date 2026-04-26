package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<User> getUsers() {
        return userService.findAll();
    }

    // Get a single user by id
    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable int userId) {
        User theUser = userService.findById(userId);

        if (theUser == null) {
            throw new RuntimeException("User not found: " + userId);
        }

        return theUser;
    }

    // Create a new User
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        User dbUser = userService.save(user);

        return dbUser;
    }

    // Update (PUT) an existing user
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        User dbUser = userService.save(user);
        return dbUser;
    }

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