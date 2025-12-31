package com.accomputers.api.infrastructure.http.controllers;

// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Application
import com.accomputers.api.application.dtos.auth.LoginDTO;
import com.accomputers.api.application.ports.input.AuthUseCase;

// Domain
import com.accomputers.api.domain.entities.User;

// Infrastructure
import com.accomputers.api.infrastructure.http.ResponseDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthUseCase authUseCase;

    @Autowired
    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<User>> login(@RequestBody LoginDTO loginDTO) {
        User user = authUseCase.login(loginDTO);

        ResponseDTO<User> responseDTO = new ResponseDTO<User>("Login successful", true, user);

        return ResponseEntity.ok(responseDTO);
    }
}
