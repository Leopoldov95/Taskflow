package com.taskflow.taskflow.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The main Spring Security configuration class.
 *
 * @Configuration — marks this as a source of Spring beans
 * @EnableWebSecurity — activates Spring Security's web support and
 *   disables the default auto-configuration (no more auto login page, etc.)
 */

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    // Our custom JWT filter (checks every request for a token)
    private final JwtAuthFilter jwtAuthFilter;

    // Our custom UserDetailsService (loads users from the DB)
    private final UserDetailsServiceImpl userDetailsService;

    public AppSecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * The main security filter chain — defines:
     *   1. CORS policy
     *   2. CSRF config
     *   3. Session policy (stateless for JWT)
     *   4. Which routes are public vs protected
     *   5. Custom error handling
     *   6. Which authentication provider to use
     *   7. Where our JWT filter fits in the chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS: delegate to corsConfigurationSource() bean defined below.
                // The cors(cors -> {}) shorthand tells Spring to find a
                // CorsConfigurationSource bean automatically.
                .cors(cors -> {})
                // CSRF protection is designed for browser-based session auth (cookies).
                // Since we're using stateless JWTs (no cookies, no sessions),
                // CSRF attacks aren't a concern here — disable it.
                .csrf(csrf -> csrf.disable())
                // STATELESS: don't create or use HTTP sessions.
                // Every request must authenticate itself via the JWT token.
                // This is fundamental to JWT-based auth.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth
                // OPTIONS requests are pre-flight CORS checks sent by browsers
                // before the real request. They must always be allowed through.
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Login/register endpoints — no token needed (e.g. how would we
                // have a token before logging in?)
                .requestMatchers("/api/auth/**").permitAll()

                // Me routes — any authenticated user can access profie
                .requestMatchers("/api/me/**").authenticated()

                // User routes — admin only
                .requestMatchers("/api/users").hasRole("ADMIN")
                .requestMatchers("/api/users/**").hasRole("ADMIN")

                // Team routes
                .requestMatchers(HttpMethod.GET, "/api/teams").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/teams/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/teams").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/teams/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/teams/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/teams/**").hasRole("ADMIN")

                // Project routes
                .requestMatchers(HttpMethod.GET, "/api/projects/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/projects/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/projects/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasRole("ADMIN")

                // Everything else requires authentication
                .anyRequest().authenticated()
        )
                // handle unauthorized access
                .exceptionHandling(ex -> ex
                        // authenticationEntryPoint: fires when an UNAUTHENTICATED user
                        // tries to access a protected route (no token, or invalid token)
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("AUTH ENTRY POINT HIT: " + authException.getMessage());
                            response.sendError(401, authException.getMessage());
                        })
                        // accessDeniedHandler: fires when an AUTHENTICATED user tries to
                        // access a route they don't have the role for
                        // (e.g. a MEMBER trying to hit an ADMIN-only endpoint)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("ACCESS DENIED: " + accessDeniedException.getMessage());
                            response.sendError(403, accessDeniedException.getMessage());
                        })
                )
                // Wire in our DaoAuthenticationProvider (DB-backed auth with BCrypt)
                .authenticationProvider(authenticationProvider())
                // Insert our JwtAuthFilter BEFORE Spring's built-in
                // UsernamePasswordAuthenticationFilter in the filter chain.
                // This ensures the JWT is processed first on every request.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // 👈 plug in our JWT filter

        return http.build();
    }

    /**
     * CORS (Cross-Origin Resource Sharing) configuration.
     *
     * Browsers block JS from calling APIs on a different origin by default.
     * This config tells Spring which origins, methods, and headers to allow.
     *
     * In production will need to replace localhost:3000 with your real frontend domain.
     * Never use "*" for allowedOrigins when allowCredentials is true.
     */    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();

        config.setAllowedOrigins(java.util.List.of("http://localhost:3000"));
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(java.util.List.of("*")); // allow all headers incl. Authorization
        config.setAllowCredentials(true); // allows cookies/auth headers cross-origin

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // apply to all routes

        return source;
    }

    /**
     * DaoAuthenticationProvider: the component that handles the actual
     * username/password check during login.
     *
     * It uses:
     *   - UserDetailsService: to load the user from DB by email
     *   - PasswordEncoder: to hash the incoming password and compare
     *     it to the BCrypt hash stored in the DB
     *
     * This is used by the AuthenticationManager (injected into your
     * AuthController) when you call authenticationManager.authenticate(...)
     * during the login flow.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
//        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * AuthenticationManager is the entry point for triggering authentication.
     * this gets injected into AuthController and call:
     *   authenticationManager.authenticate(
     *     new UsernamePasswordAuthenticationToken(email, password)
     *   )
     * Spring then delegates to the DaoAuthenticationProvider above.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * BCrypt password encoder.
     * Passwords are NEVER stored in plain text — BCrypt hashes them with a salt.
     * BCrypt is slow by design (cost factor), making brute-force attacks expensive.
     *
     * Usage: passwordEncoder.encode("rawPassword") when creating a user
     *        (Spring does the comparison automatically during login)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}