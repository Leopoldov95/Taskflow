package com.taskflow.taskflow.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A servlet filter that intercepts every HTTP request and checks for a JWT.
 *
 * Extends OncePerRequestFilter — Spring guarantees this filter runs exactly
 * once per request (some filters can run multiple times due to forward/include
 * dispatches; this prevents that).
 *
 * Important: This filter does NOT reject requests. It either authenticates the
 * user (if a valid token is present) or does nothing (if no token or invalid
 * token). The actual rejection of unauthorized requests is handled downstream
 * by Spring Security's authorization rules defined in AppSecurityConfig.
 */

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain  // the rest of the filter chain + your controller
    )
            throws ServletException, IOException {

        // STEP 1: Read the Authorization header from the incoming request.
        // For JWT, the convention is:  Authorization: Bearer eyJhbGci...
        final String authHeader = request.getHeader("Authorization");

        // STEP 2: Early exit — if there's no token at all, just pass the request
        // along unchanged. Spring Security will handle rejection if the route
        // requires authentication (via the rules in AppSecurityConfig).
        // This is the path taken for public routes like POST /api/auth/login.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // STEP 3: Strip the "Bearer " prefix (7 characters) to get just the token.
        // e.g. "Bearer eyJhbGci..." → "eyJhbGci..."
        final String token = authHeader.substring(7);

        // STEP 4: Decode the token to find out WHO it belongs to.
        // This calls JwtService which uses JJWT to parse and verify the token.
        // If the token is malformed or has a bad signature, an exception is
        // thrown here. You'd want a try/catch in production to return a clean 401.
        final String email = jwtService.extractUsername(token);

        // STEP 5: We only proceed if:
        //   a) We successfully extracted an email from the token, AND
        //   b) The SecurityContext doesn't already have authentication set
        //      (meaning this request hasn't been authenticated yet)
        //
        // The SecurityContextHolder is a thread-local store — it holds the
        // authentication for the current request's thread. It starts null
        // for every new request.
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the full user object from the DB using the email from the token.
            // This is needed to get roles/authorities and to do the final validation.
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Check two things:
            //   1. The email in the token matches this user (sanity check)
            //   2. The token hasn't expired
            if (jwtService.isTokenValid(token, userDetails)) {

                /**
                 * Create Spring Security's authentication object.
                 * UsernamePasswordAuthenticationToken is the standard class for
                 * representing an authenticated user.
                 *
                 * Constructor: (principal, credentials, authorities)
                 *   principal:   the UserDetails object (who is logged in)
                 *   credentials: null — we don't need the password anymore at this point
                 *   authorities: the user's roles (e.g. ROLE_ADMIN, ROLE_MEMBER)
                 *
                 * Passing authorities to the 3-arg constructor is what marks this
                 * token as "authenticated = true". The 2-arg constructor (no authorities)
                 * creates an unauthenticated token.
                 */
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Attach extra request details (IP address, session ID) to the
                // auth token. Useful for audit logging and some security checks.
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // STEP 6: Store the authentication in the SecurityContext.
                // This is what tells the rest of Spring Security "this request
                // belongs to an authenticated user." After this line, any call to
                // SecurityContextHolder.getContext().getAuthentication() in the
                // controllers or services will return this auth object.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // STEP 7: Always continue down the filter chain regardless of outcome.
        // If we authenticated → the security rules will allow access to protected routes.
        // If we didn't → the security rules will reject access to protected routes.
        // This filter never directly sends a 401/403 itself.
        filterChain.doFilter(request, response);
    }
}
