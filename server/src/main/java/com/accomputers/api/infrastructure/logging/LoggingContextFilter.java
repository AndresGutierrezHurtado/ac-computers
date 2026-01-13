package com.accomputers.api.infrastructure.logging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.accomputers.api.application.ports.output.UserAuthServiceInterface;
import com.accomputers.api.domain.entities.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingContextFilter extends OncePerRequestFilter {

    private final UserAuthServiceInterface userAuthServiceInterface;

    @Autowired
    public LoggingContextFilter(UserAuthServiceInterface userAuthServiceInterface) {
        this.userAuthServiceInterface = userAuthServiceInterface;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            MDC.put("requestId", UUID.randomUUID().toString());
            MDC.put("ip", request.getRemoteAddr());
            MDC.put("method", request.getMethod());
            MDC.put("path", request.getRequestURI());

            User user = userAuthServiceInterface.getAuthenticatedUser();

            if (user != null) {
                MDC.put("userId", user.getId().toString());
            }

            filterChain.doFilter(request, response);

        } finally {
            MDC.clear();
        }
    }
}
