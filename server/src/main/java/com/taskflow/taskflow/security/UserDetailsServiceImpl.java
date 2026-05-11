package com.taskflow.taskflow.security;

import com.taskflow.taskflow.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // My JPA repository for hitting the `users` table
    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Spring Security calls this method whenever it needs to load a user.
     * Despite the method name saying "Username", we're using email as the
     * unique identifier. Spring doesn't care what field I use — it just
     * calls whatever I put here.
     *
     * Important: My User entity must implement the UserDetails interface
     * for this return type to work. That's what lets Spring Security
     * understand your User object (getPassword(), getAuthorities(), etc.)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try to find user by email. If not found, throw the specific
        // exception Spring Security expects — it will translate this into
        // a 401 Unauthorized response automatically.
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}