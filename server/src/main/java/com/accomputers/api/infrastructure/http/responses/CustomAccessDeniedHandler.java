package com.accomputers.api.infrastructure.http.responses;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.accomputers.api.infrastructure.http.GlobalExceptionHandler;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    public CustomAccessDeniedHandler(GlobalExceptionHandler globalExceptionHandler) {
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        globalExceptionHandler.writeForbidden(response);
    }
}

