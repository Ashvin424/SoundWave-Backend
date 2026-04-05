package com.ashvinprajapati.soundwave.auth.controller;

import com.ashvinprajapati.soundwave.auth.dto.*;
import com.ashvinprajapati.soundwave.auth.entity.User;
import com.ashvinprajapati.soundwave.auth.service.AuthService;
import com.ashvinprajapati.soundwave.auth.service.CustomUserDetailService;
import com.ashvinprajapati.soundwave.auth.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CustomUserDetailService customUserDetailService;
    private final PasswordResetService passwordResetService;

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

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal User user
            ) {
        try {
            authService.changePassword(request, user);
            return ResponseEntity.ok("Password Changed Successfully");
        } catch (RuntimeException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email
    ) {
        try {
            passwordResetService.initiatePasswordReset(email);
            return ResponseEntity.ok("Password reset email sent if the email exists in our system.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword
    ) {
        try {
            passwordResetService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password reset successful");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody UpdateProfileRequest request,
            Authentication authentication
    ) {
        try {
            String email = authentication.getName();
            User user = (User) customUserDetailService.loadUserByUsername(email);
            System.out.println("Controller reached, user: " + user); // debug
            authService.updateProfile(request, user);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage()); // debug
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
