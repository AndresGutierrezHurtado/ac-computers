package com.accomputers.api.infrastructure.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.accomputers.api.application.ports.output.UserAuthServiceInterface;
import com.accomputers.api.application.ports.output.repositories.UserRepositoryInterface;
import com.accomputers.api.domain.entities.User;

@Service
public class JwtUserAuthService implements UserAuthServiceInterface {

    private final UserRepositoryInterface userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtUserAuthService(UserRepositoryInterface userRepository, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void authenticateUser(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail().getValue(),
                null,
                UserAuthoritiesMapper.map(user));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(user.getId());

        HttpServletResponse response = getHttpServletResponse();

        if (response != null) {
            response.setHeader("Authorization", token);
        }
    }

    @Override
    public User getAuthenticatedUser() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return null;
        }

        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            return null;
        }

        try {
            String userIdStr = jwtUtil.getUserIdFromToken(token);
            Integer userId = Integer.parseInt(userIdStr);
            return userRepository.findById(userId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void refreshSession(User user) {
        if (user == null || user.getId() == null) {
            return;
        }

        String newToken = jwtUtil.generateToken(user.getId());
        HttpServletResponse response = getHttpServletResponse();
        if (response != null) {
            response.setHeader("Authorization", newToken);
        }

        // Update authentication context
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail().getValue(),
                null,
                UserAuthoritiesMapper.map(user));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void revokeSession(User user) {
        SecurityContextHolder.clearContext();

        HttpServletResponse response = getHttpServletResponse();
        if (response != null) {
            response.setHeader("Authorization", "");
        }
    }

    @Override
    public void invalidateAllSessions(User user) {
        SecurityContextHolder.clearContext();

        HttpServletResponse response = getHttpServletResponse();
        if (response != null) {
            response.setHeader("Authorization", "");
        }
    }

    @Override
    public void logoutUser(User user) {
        SecurityContextHolder.clearContext();

        HttpServletResponse response = getHttpServletResponse();
        if (response != null) {
            response.setHeader("Authorization", "");
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getResponse() : null;
    }
}
