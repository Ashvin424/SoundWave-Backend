package com.ashvinprajapati.soundwave.song.repository;

import com.ashvinprajapati.soundwave.auth.entity.User;
import com.ashvinprajapati.soundwave.song.entity.RecentPlayed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecentlyPlayedRepository extends JpaRepository<RecentPlayed, Long> {
    List<RecentPlayed> findTop10ByUserOrderByPlayedAtDesc(User user);

    Optional<RecentPlayed> findByUserAndSong_Id(User user, Long songId);

    long countByUser(User user);
}
