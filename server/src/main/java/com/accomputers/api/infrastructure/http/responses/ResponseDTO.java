package com.accomputers.api.infrastructure.http.responses;

public record ResponseDTO<T>(String message, boolean success, T data) {
    public ResponseDTO(String message, boolean success) {
        this(message, success, null);
    }
}
