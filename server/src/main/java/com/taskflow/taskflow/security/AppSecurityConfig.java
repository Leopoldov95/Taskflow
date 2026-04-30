package com.taskflow.taskflow.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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

        // define query to retrieve a user by username, we will use email as username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select email, password, is_active from users where email=?");

        // define query to retrieve the authorities/roles by username
        // must use a JOIN query as the user_role is found in a separate table
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
                        .requestMatchers(HttpMethod.PATCH, "/api/users/**").hasRole("ADMIN")
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
                        // Fallback routes
                        .anyRequest().permitAll()
        );

        // use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // disable CSRF
        // in general, not required for stateless REST APIs that use POST PUT, DELETE, and/or PATCH

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    // updated method to allow use of {bcrypt} as stored value in User DB
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //return new BCryptPasswordEncoder();
    }
}
