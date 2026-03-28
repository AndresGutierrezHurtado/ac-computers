package com.accomputers.api.infrastructure.http;

import java.io.IOException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.fasterxml.jackson.databind.ObjectMapper;

// Domain
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.exceptions.InvalidCredentialsException;
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;
import com.accomputers.api.application.ports.output.LoggerPort;
import com.accomputers.api.domain.exceptions.DomainException;

// Responses
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String MESSAGE_UNAUTHENTICATED = "Usuario no autenticado";
    public static final String MESSAGE_FORBIDDEN = "Usuario sin permisos";
    public static final String MESSAGE_RATE_LIMITED = "Too many requests";

    private final LoggerPort logger;
    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(LoggerPort loggerPort, ObjectMapper objectMapper) {
        this.logger = loggerPort;
        this.objectMapper = objectMapper;
    }

    /**
     * Escribe el mismo cuerpo JSON que los {@code @ExceptionHandler}, para filtros y Spring Security
     * (donde no aplica {@code RestControllerAdvice}).
     */
    public void writeErrorResponse(HttpServletResponse response, HttpStatus status, String message)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), new ResponseDTO<Void>(message, false));
    }

    public void writeUnauthenticated(HttpServletResponse response) throws IOException {
        writeErrorResponse(response, HttpStatus.UNAUTHORIZED, MESSAGE_UNAUTHENTICATED);
    }

    public void writeForbidden(HttpServletResponse response) throws IOException {
        writeErrorResponse(response, HttpStatus.FORBIDDEN, MESSAGE_FORBIDDEN);
    }

    public void writeRateLimited(HttpServletResponse response) throws IOException {
        writeErrorResponse(response, HttpStatus.TOO_MANY_REQUESTS, MESSAGE_RATE_LIMITED);
    }

    private ResponseEntity<ResponseDTO<Void>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ResponseDTO<>(message, false));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseDTO<Void>> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(InvalidValueObjectException.class)
    public ResponseEntity<ResponseDTO<Void>> handleInvalidValueObjectException(InvalidValueObjectException e) {
        return error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream().findFirst().get().getMessage();
        return error(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return error(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ResponseDTO<Void>> handleDomainException(DomainException e) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseDTO<Void>> handleDataAccessException(DataAccessException e) {
        if (e.getMessage().toLowerCase().contains("No se puede agregar o actualizar una fila hija: una restricción de clave foránea falla")) {
            return error(HttpStatus.BAD_REQUEST,
                    "La entidad no puede ser manipulada porque tiene una restricción de clave foránea a otra entidad");
        }

        logger.error("Ocurrió un error con la base de datos: " + e.getMessage(), e);
        return error(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error con la base de datos, verifique los logs del servidor para más información");
    }

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleFileUploadingError(Exception e) {
        logger.error("Ocurrió un error al subir el archivo: " + e.getMessage(), e);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleException(Exception e) {
        logger.error("Ocurrió un error inesperado: " + e.getMessage(), e);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }
}
