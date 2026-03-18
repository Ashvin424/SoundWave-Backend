package com.ashvinprajapati.soundwave.auth.controller;

import com.ashvinprajapati.soundwave.auth.dto.AuthResponse;
import com.ashvinprajapati.soundwave.auth.dto.LoginRequest;
import com.ashvinprajapati.soundwave.auth.dto.RegisterRequest;
import com.ashvinprajapati.soundwave.auth.entity.User;
import com.ashvinprajapati.soundwave.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
            ) {
        try {
            AuthResponse createdUser = authService.register(request);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest loginRequest
            ) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
