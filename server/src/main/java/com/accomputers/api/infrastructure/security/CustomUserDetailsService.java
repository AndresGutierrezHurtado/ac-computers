package com.accomputers.api.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.accomputers.api.application.ports.output.repositories.UserRepositoryInterface;
import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.valueobjects.Email;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryInterface userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepositoryInterface userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(new Email(email));
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail().getValue())
                .password(user.getPassword().getValue())
                .authorities(UserAuthoritiesMapper.map(user))
                .build();
    }
}

