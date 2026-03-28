package com.accomputers.api.infrastructure.http.responses;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.accomputers.api.infrastructure.http.GlobalExceptionHandler;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    public CustomAuthenticationEntryPoint(GlobalExceptionHandler globalExceptionHandler) {
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        globalExceptionHandler.writeUnauthenticated(response);
    }
}

