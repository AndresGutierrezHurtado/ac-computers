package com.accomputers.api.infrastructure.security;

import com.accomputers.api.application.ports.output.UserAuthServiceInterface;
import com.accomputers.api.domain.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtUserAuthService implements UserAuthServiceInterface {

    private final SecretKey secretKey;
    private final long expirationTimeInMinutes;
    private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();
    private final Set<Integer> invalidatedUsers = ConcurrentHashMap.newKeySet();

    public JwtUserAuthService(
            @Value("${jwt.secret:your-256-bit-secret-key-must-be-at-least-32-characters-long}")
            String jwtSecret,
            @Value("${jwt.expiration:1440}")
            long expirationTimeInMinutes) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.expirationTimeInMinutes = expirationTimeInMinutes;
    }

    @Override
    public void authenticateUser(User user) {
        // Token generation is handled by generateToken method
        // This method can be used to track authentication events
    }

    @Override
    public void refreshSession(User user) {
        // Remove user from invalidated list if present
        invalidatedUsers.remove(user.getId());
    }

    @Override
    public void revokeSession(User user) {
        // This would typically revoke a specific token
        // For now, we'll invalidate all sessions for the user
        invalidateAllSessions(user);
    }

    @Override
    public void invalidateAllSessions(User user) {
        if (user != null && user.getId() != null) {
            invalidatedUsers.add(user.getId());
        }
    }

    @Override
    public void logoutUser(User user) {
        invalidateAllSessions(user);
    }

    public String generateToken(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and user ID cannot be null");
        }

        Instant now = Instant.now();
        Instant expiration = now.plus(expirationTimeInMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail().getValue())
                .claim("roleId", user.getRoleId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        // Check if token is in revoked list
        if (revokedTokens.contains(token)) {
            throw new SecurityException("Token has been revoked");
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Check if user's sessions have been invalidated
            Integer userId = Integer.parseInt(claims.getSubject());
            if (invalidatedUsers.contains(userId)) {
                throw new SecurityException("User sessions have been invalidated");
            }

            return claims;
        } catch (Exception e) {
            throw new SecurityException("Invalid token: " + e.getMessage());
        }
    }

    public void revokeToken(String token) {
        if (token != null && !token.isEmpty()) {
            revokedTokens.add(token);
        }
    }

    public Integer getUserIdFromToken(String token) {
        Claims claims = validateToken(token);
        return Integer.parseInt(claims.getSubject());
    }
}

