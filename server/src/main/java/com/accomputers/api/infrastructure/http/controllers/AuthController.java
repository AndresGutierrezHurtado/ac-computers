package com.accomputers.api.infrastructure.http.controllers;

// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

// Application
import com.accomputers.api.application.dtos.auth.ForgotPasswordDTO;
import com.accomputers.api.application.dtos.auth.GoogleLoginDTO;
import com.accomputers.api.application.dtos.auth.LoginDTO;
import com.accomputers.api.application.dtos.auth.RegisterDTO;
import com.accomputers.api.application.dtos.auth.SetPasswordDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;
import com.accomputers.api.application.ports.input.AuthServiceInterface;

// Infrastructure
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final String FORGOT_PASSWORD_MESSAGE =
            "Si el correo está registrado, recibirás instrucciones en breve.";

    private final AuthServiceInterface authServiceInterface;

    @Autowired
    public AuthController(AuthServiceInterface authServiceInterface) {
        this.authServiceInterface = authServiceInterface;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> login(@RequestBody @Valid LoginDTO loginDTO) {
        UserResponseDTO user = authServiceInterface.login(loginDTO);

        ResponseDTO<UserResponseDTO> responseDTO = new ResponseDTO<UserResponseDTO>("Login successful", true, user);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> register(@RequestBody @Valid RegisterDTO registerDTO) {
        UserResponseDTO user = authServiceInterface.register(registerDTO);

        ResponseDTO<UserResponseDTO> responseDTO = new ResponseDTO<UserResponseDTO>("User registered successfully",
                true, user);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/google")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> googleLogin(@RequestBody @Valid GoogleLoginDTO googleLoginDTO) {
        UserResponseDTO user = authServiceInterface.googleLogin(googleLoginDTO);
        ResponseDTO<UserResponseDTO> responseDTO = new ResponseDTO<>("Login successful", true, user);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/session")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> getSession() {
        UserResponseDTO user = authServiceInterface.getSession();

        ResponseDTO<UserResponseDTO> responseDTO = new ResponseDTO<UserResponseDTO>("Session retrieved successfully",
                true, user);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO<Void>> logout() {
        authServiceInterface.logout();

        ResponseDTO<Void> responseDTO = new ResponseDTO<Void>("Logout successful", true);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/set-password")
    public ResponseEntity<ResponseDTO<Void>> setPassword(@RequestBody @Valid SetPasswordDTO setPasswordDTO) {
        authServiceInterface.setPassword(setPasswordDTO);

        ResponseDTO<Void> responseDTO = new ResponseDTO<Void>("Password updated successfully", true);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO<Void>> forgotPassword(@RequestBody @Valid ForgotPasswordDTO forgotPasswordDTO) {
        authServiceInterface.requestPasswordReset(forgotPasswordDTO);

        ResponseDTO<Void> responseDTO = new ResponseDTO<Void>(FORGOT_PASSWORD_MESSAGE, true);

        return ResponseEntity.ok(responseDTO);
    }
}
