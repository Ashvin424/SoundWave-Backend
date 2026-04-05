package com.ashvinprajapati.soundwave.auth.service;

import com.ashvinprajapati.soundwave.auth.dto.*;
import com.ashvinprajapati.soundwave.auth.entity.Role;
import com.ashvinprajapati.soundwave.auth.entity.User;
import com.ashvinprajapati.soundwave.auth.repository.UserRepository;
import com.ashvinprajapati.soundwave.common.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .enabled(true)
                .role(Role.USER)
                .build();

            userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        String token = jwtService.generateToken(loginRequest.getEmail());

        return new AuthResponse(token);
    }

    public void changePassword(ChangePasswordRequest request, User user) {
        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void updateProfile(UpdateProfileRequest request, User user) {

        System.out.println("Updating profile for: " + user.getEmail());
        System.out.println("New name: " + request.getFullName());

        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new RuntimeException("Full name cannot be empty");
        }
        user.setFullName(request.getFullName());
        userRepository.save(user);
    }
}
