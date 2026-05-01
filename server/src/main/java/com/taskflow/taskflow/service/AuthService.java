package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.RoleRepository;
import com.taskflow.taskflow.dao.UserRepository;
import com.taskflow.taskflow.dto.auth.AuthResponse;
import com.taskflow.taskflow.dto.auth.LoginRequest;
import com.taskflow.taskflow.dto.auth.RegisterRequest;
import com.taskflow.taskflow.entity.Role;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.entity.enums.RoleType;
import com.taskflow.taskflow.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // register a new user and return a token
    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // default role
        Role role = roleRepository.findByName(RoleType.ROLE_MEMBER.name());
        user.setRoles(Set.of(role));

        userRepository.save(user);

        // generate token immediately so user is logged in after registering
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    // Login and return a token
    public AuthResponse login(LoginRequest request) {
        // this checks credentials and throws an exception if invalid
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // credentials are valid, load user and generate token
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
