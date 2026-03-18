package com.ashvinprajapati.soundwave.playlist.repository;

import com.ashvinprajapati.soundwave.auth.entity.User;
import com.ashvinprajapati.soundwave.playlist.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
    List<PlaylistEntity> findByOwner(User owner);
}
