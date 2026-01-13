package com.accomputers.api.infrastructure.logging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            MDC.put("requestId", UUID.randomUUID().toString());
            MDC.put("ip", request.getRemoteAddr());
            MDC.put("method", request.getMethod());
            MDC.put("path", request.getRequestURI());

            if (request.getUserPrincipal() != null) {
                MDC.put("user", request.getUserPrincipal().getName());
            }

            filterChain.doFilter(request, response);

        } finally {
            MDC.clear();
        }
    }
}
