package com.ashvinprajapati.soundwave.song.service;

import com.ashvinprajapati.soundwave.auth.entity.User;
import com.ashvinprajapati.soundwave.song.dto.SongResponse;
import com.ashvinprajapati.soundwave.song.entity.RecentPlayed;
import com.ashvinprajapati.soundwave.song.entity.SongEntity;
import com.ashvinprajapati.soundwave.song.repository.RecentlyPlayedRepository;
import com.ashvinprajapati.soundwave.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecentlyPlayedService {

    private final RecentlyPlayedRepository recentlyPlayedRepository;
    private final SongRepository songRepository;

    public void markAsPlayed(Long songId, User user) {
        SongEntity song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        Optional<RecentPlayed> existing = recentlyPlayedRepository.findByUserAndSong_Id(user, songId);

        if (existing.isPresent()) {
            existing.get().setPlayedAt(LocalDateTime.now());
            recentlyPlayedRepository.save(existing.get());
        } else {
            recentlyPlayedRepository.save(
                    RecentPlayed.builder()
                            .user(user)
                            .song(song)
                            .playedAt(LocalDateTime.now())
                            .build()
            );
        }
    }
    
    public List<SongResponse> getRecentlyPlayed(User user) {
        return recentlyPlayedRepository
                .findTop10ByUserOrderByPlayedAtDesc(user)
                .stream()
                .map(rp -> SongResponse.builder()
                        .id(rp.getSong().getId())
                        .title(rp.getSong().getTitle())
                        .artist(rp.getSong().getArtist())
                        .audioUrl(rp.getSong().getAudioUrl())
                        .coverImageUrl(rp.getSong().getCoverImageUrl())
                        .duration(rp.getSong().getDuration())
                        .album(rp.getSong().getAlbum())
                        .genre(rp.getSong().getGenre())
                        .build())
                .toList();
    }

    public long getPlayedCount(User user) {
        return recentlyPlayedRepository.countByUser(user);
    }
}
