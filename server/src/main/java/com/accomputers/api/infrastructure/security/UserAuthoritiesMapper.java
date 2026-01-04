package com.accomputers.api.infrastructure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.accomputers.api.domain.entities.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class UserAuthoritiesMapper {

    private UserAuthoritiesMapper() {}

    public static Collection<GrantedAuthority> map(User user) {
        if (user == null) {
            return Collections.emptyList();
        }

        if (user.getRole() != null && user.getRole().getName() != null) {
            String authority = "ROLE_" + user.getRole().getName().toUpperCase();
            return List.of(new SimpleGrantedAuthority(authority));
        }

        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}

