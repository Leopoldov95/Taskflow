package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.RoleRepository;
import com.taskflow.taskflow.dao.UserRepository;
import com.taskflow.taskflow.entity.Role;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.entity.enums.RoleType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
            throw new RuntimeException("Did not find user with id: " + id);
        }
        return theUser;
    }

    //TODO ~ implement logic to find by email

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        User theUser = null;
        if (user.isPresent()) {
            theUser = user.get();
        } else {
            throw new RuntimeException("Did not find user with email: " + email);
        }

        return theUser;
    }

//    @Override
//    public User save(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }

    @Transactional
    @Override
    public User save(User user) {
        // 🔐 encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 💾 save user first
        User savedUser = userRepository.save(user);

        // 👤 assign default role
        Role role = roleRepository.findByName(RoleType.ROLE_MEMBER.name());

        savedUser.setRoles(Set.of(role));

        return userRepository.save(savedUser);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
}