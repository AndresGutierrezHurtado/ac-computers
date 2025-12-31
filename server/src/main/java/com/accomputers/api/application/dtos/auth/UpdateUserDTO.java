package com.accomputers.api.application.dtos.auth;

public record UpdateUserDTO(String firstName, String lastName, String email, Integer roleId) {
}

