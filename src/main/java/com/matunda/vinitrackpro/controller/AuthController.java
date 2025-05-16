package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.AuthResponseDTO;
import com.matunda.vinitrackpro.dto.LoginRequestDTO;
import com.matunda.vinitrackpro.dto.SignupRequestDTO;
import com.matunda.vinitrackpro.dto.UserDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuditLogService auditLogService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        AuthResponseDTO response = authService.authenticateUser(loginRequest);
        auditLogService.logActivity("LOGIN", "User", response.getUser().getId(),
                "User logged in: " + response.getUser().getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody SignupRequestDTO signupRequest) {
        UserDTO user = authService.registerUser(signupRequest);
        auditLogService.logActivity("REGISTER", "User", user.getId(),
                "New user registered: " + user.getUsername());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
