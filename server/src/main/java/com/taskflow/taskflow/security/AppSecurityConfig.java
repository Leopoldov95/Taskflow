package com.taskflow.taskflow.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class AppSecurityConfig {

    // add support for JDBC ... no more hardcoded users :-)

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Use BCryptPasswordEncoder to hash passwords before storing them
        //TODO below method does not exist
       // jdbcUserDetailsManager.setPasswordEncoder(new BCryptPasswordEncoder());

        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select email, password, is_active from users where email=?");

        // define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.email, r.role\n" +
                        "FROM roles r\n" +
                        "JOIN user_role ur ON r.id = ur.role_id\n" +
                        "JOIN users u ON u.id = ur.user_id\n" +
                        "WHERE u.email = ?");

        return jdbcUserDetailsManager;
    }

    // restricting access based on roles
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        // User Routes
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        // Team routes
                        .requestMatchers(HttpMethod.GET, "/api/teams").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.GET, "/api/teams/**").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.POST, "/api/teams").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/teams").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/teams/**").hasRole("ADMIN")
                        // Project routes
                        .requestMatchers(HttpMethod.GET, "/api/projects").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.POST, "/api/projects").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/projects").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasRole("ADMIN")
                        // Task routes
                        .requestMatchers(HttpMethod.GET, "/error").hasRole("MEMBER")
        );

        // use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // disable CSRF
        // in general, not required for stateless REST APIs that use POST PUT, DELETE, and/or PATCH

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
