package com.accomputers.api.application.dtos.auth;

public record RegisterDTO(String firstName, String lastName, String email, String password, Integer roleId) {
}

