package com.accomputers.api.infrastructure.http;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Domain
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.exceptions.InvalidValueObjectException;
import com.accomputers.api.domain.exceptions.DomainException;

// Responses
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<Void>(e.getMessage(), false));
    }

    @ExceptionHandler(InvalidValueObjectException.class)
    public ResponseEntity<ResponseDTO<Void>> handleInvalidValueObjectException(InvalidValueObjectException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<Void>(e.getMessage(), false));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ResponseDTO<Void>> handleDomainException(DomainException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<Void>(e.getMessage(), false));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseDTO<Void>> handleSQLException(SQLException e) {
        System.out.println("SQLException: " + e.getMessage() + " Stack Trace: " + e.getStackTrace());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<Void>("There was an error with the database, check the server logs for more information", false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleException(Exception e) {
        System.out.println("SQLException: " + e.getMessage() + " Stack Trace: " + e.getStackTrace());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<Void>("Internal server error", false));
    }
}
