package com.ashvinprajapati.soundwave.auth.service;

import com.ashvinprajapati.soundwave.auth.entity.PasswordResetToken;
import com.ashvinprajapati.soundwave.auth.entity.User;
import com.ashvinprajapati.soundwave.auth.repository.PasswordResetTokenRepository;
import com.ashvinprajapati.soundwave.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow( () -> new RuntimeException("User Not Found with email: " + email) );

        passwordResetTokenRepository.deleteByUser(user);
        passwordResetTokenRepository.flush();

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();

        passwordResetTokenRepository.save(resetToken);
        sendResetEmail(user.getEmail(), token);
    }

    @Transactional
    public void sendResetEmail(String email, String token) {
        String resetUrl = "http://10.17.73.140:8080/reset-password.html?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("Click the link to reset your password: \n" +resetUrl + "\nThis link will expire in 1 hour.");

        javaMailSender.send(message);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow( () -> new RuntimeException("Invalid Password Reset Token") );

      if (resetToken.isExpired()) {
          passwordResetTokenRepository.delete(resetToken);
            throw new RuntimeException("Password Reset Token has expired");
      }

      User user = resetToken.getUser();
      user.setPassword(passwordEncoder.encode(newPassword));
      userRepository.save(user);

      passwordResetTokenRepository.delete(resetToken);
    }
}
