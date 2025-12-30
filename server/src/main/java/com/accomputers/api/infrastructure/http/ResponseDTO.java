package com.accomputers.api.infrastructure.http;

public record ResponseDTO<T>(String message, boolean success, T data, Long total) {
    public ResponseDTO(String message, boolean success) {
        this(message, success, null, null);
    }

    public ResponseDTO(String message, boolean success, T data) {
        this(message, success, data, null);
    }
}
