package com.accomputers.api.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.accomputers.api.application.ports.output.repositories.UserRepositoryInterface;
import com.accomputers.api.domain.entities.User;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepositoryInterface userRepository;

    @Autowired
    public JwtAuthenticationFilter(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = JwtUtil.extractTokenFromRequest(request);

        if (token != null && JwtUtil.validateToken(token)) {
            try {
                String userIdStr = JwtUtil.getUserIdFromToken(token);
                Integer userId = Integer.parseInt(userIdStr);
                User user = userRepository.findById(userId);

                if (user != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user.getEmail().getValue(),
                            null,
                            UserAuthoritiesMapper.map(user)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Invalid token or user not found, continue without authentication
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}

