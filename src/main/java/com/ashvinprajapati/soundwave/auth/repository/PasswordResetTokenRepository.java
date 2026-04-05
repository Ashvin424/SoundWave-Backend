package com.ashvinprajapati.soundwave.auth.repository;

import com.ashvinprajapati.soundwave.auth.entity.PasswordResetToken;
import com.ashvinprajapati.soundwave.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);

}
