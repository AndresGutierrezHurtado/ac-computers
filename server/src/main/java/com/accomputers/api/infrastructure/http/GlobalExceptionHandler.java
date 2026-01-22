package com.accomputers.api.infrastructure.http;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

// Domain
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;
import com.accomputers.api.application.ports.output.LoggerPort;
import com.accomputers.api.domain.exceptions.DomainException;

// Responses
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final LoggerPort logger;

    public GlobalExceptionHandler(LoggerPort loggerPort) {
        this.logger = loggerPort;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<Void>(e.getMessage(), false));
    }

    @ExceptionHandler(InvalidValueObjectException.class)
    public ResponseEntity<ResponseDTO<Void>> handleInvalidValueObjectException(InvalidValueObjectException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<Void>(e.getMessage(), false));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream().findFirst().get().getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<Void>(errorMessage, false));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<Void>(errorMessage, false));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ResponseDTO<Void>> handleDomainException(DomainException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO<Void>(e.getMessage(), false));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseDTO<Void>> handleDataAccessException(DataAccessException e) {
        if (e.getMessage().toLowerCase().contains("cannot add or update a child row: a foreign key constraint fails")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<Void>(
                    "The entity cannot be manipulated because it has a foreign key constraint to another entity",
                    false));
        }

        logger.error("Database error: " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<Void>(
                "There was an error with the database, check the server logs for more information", false));
    }

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleFileUploadingError(Exception e) {
        logger.error("Failed to upload attachment: " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO<Void>(e.getMessage(), false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleException(Exception e) {
        logger.error("Unexpected exception occurred: " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO<Void>("Internal server error", false));
    }
}
