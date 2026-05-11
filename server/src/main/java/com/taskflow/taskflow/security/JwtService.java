package com.taskflow.taskflow.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    /**
     * The secret key used to SIGN tokens (HMAC-SHA algorithm).
     * Pulled from application.properties via @Value.
     *
     * This must be kept secret — anyone with this key can forge valid tokens.
     * In production this loads from an environment variable or
     * a secrets manager (AWS Secrets Manager, Vault, etc.), not a
     * committed properties file.
     */
    // secret key to sign tokens
    @Value("${security.jwt.secret}")
    private String secretKey;

    // Token valid for 24 hours
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    /**
     * Public entry point: generate a plain token with no extra claims.
     * This is what AuthController would call after login succeeds.
     * Delegates to the overloaded version below with an empty claims map.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * The real token builder. Uses the JJWT library (io.jsonwebtoken).
     *
     * extraClaims: any additional data I want embedded in the token payload,
     * e.g. { "role": "ADMIN" }. These are readable by the client (they're
     * just Base64-encoded, NOT encrypted), so never put sensitive data here.
     *
     * The token payload (claims) will contain:
     *   - Any extraClaims passed in
     *   - sub: the email (subject — who this token is "about")
     *   - iat: issued-at timestamp
     *   - exp: expiration timestamp
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)                  // custom payload data (roles, etc.)
                .subject(userDetails.getUsername())   // "sub" claim — stores the email
                .issuedAt(new Date())                 // "iat" claim — now
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // "exp"
                .signWith(getSigningKey())            // signs with HMAC-SHA using your secret
                .compact();                           // serializes to the "xxx.yyy.zzz" string
    }

    /**
     * The two conditions for a valid token:
     *   1. The email embedded in the token matches the user we loaded from the DB
     *   2. The token hasn't expired
     *
     * Note: signature verification happens implicitly inside extractAllClaims()
     * via the JJWT parser — if the signature is invalid, it throws an exception
     * before we even get to these checks.
     */    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Pulls the "sub" (subject) claim from the token — which we set to the email.
     * This is called by JwtAuthFilter to know which user to look up.
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // -------------------------
    // Private helper methods
    // -------------------------


    /**
     * Checks the "exp" claim against the current time.
     * Returns true if the token's expiry date is in the past.
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * The core parsing method. Does two things in one:
     *   1. Verifies the signature — if someone tampered with the token,
     *      the JJWT library throws a SignatureException here.
     *   2. Returns the Claims object (the decoded payload) so we can
     *      read sub, exp, iat, and any custom claims.
     *
     * If the token is expired, malformed, or has a bad signature,
     * an exception is thrown and propagates up to the filter.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // tells the parser what key to verify against
                .build()
                .parseSignedClaims(token)   // does the actual verification + parsing
                .getPayload();              // returns just the claims portion
    }

    /**
     * Converts the raw secret string (from application.properties) into a
     * SecretKey object that JJWT understands.
     *
     * Keys.hmacShaKeyFor() will automatically select the right SHA algorithm
     * based on the key length (SHA-256, 384, or 512).
     *
     * The secret in the properties file is Base64 — note that we're calling
     * .getBytes() directly here, meaning we're using the raw UTF-8 bytes of
     * the Base64 string, not decoding it first.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
