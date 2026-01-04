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
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(UserRepositoryInterface userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtil.extractTokenFromRequest(request);

        if (token != null && jwtUtil.validateToken(token)) {
            try {
                String userIdStr = jwtUtil.getUserIdFromToken(token);
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
                System.out.println("Error retrieving user from token: " + e.getMessage());
                e.printStackTrace();
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}

