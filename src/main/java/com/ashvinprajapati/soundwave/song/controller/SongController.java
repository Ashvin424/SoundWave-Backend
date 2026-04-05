package com.ashvinprajapati.soundwave.song.controller;

import com.ashvinprajapati.soundwave.auth.entity.User;
import com.ashvinprajapati.soundwave.song.dto.SongResponse;
import com.ashvinprajapati.soundwave.song.dto.SongsPageResponse;
import com.ashvinprajapati.soundwave.song.entity.SongEntity;
import com.ashvinprajapati.soundwave.song.service.RecentlyPlayedService;
import com.ashvinprajapati.soundwave.song.service.SongService;
import com.ashvinprajapati.soundwave.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;
    private final StorageService storageService;
    private final RecentlyPlayedService recentlyPlayedService;

    @GetMapping
    public List<SongEntity> getAllSongs() {
        return songService.getAllSongs();
    }

    @GetMapping("/search")
    public ResponseEntity<SongsPageResponse> searchSongs(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        SongsPageResponse response = songService.searchSongs(q, page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<SongEntity> uploadSong(
            @RequestParam String title,
            @RequestParam String artist,
            @RequestParam(required = false) String album,
            @RequestParam(required = false) String genre,
            @RequestParam Long duration,
            @RequestParam MultipartFile audio,
            @RequestParam(required = false) MultipartFile cover
    ) {


        // Upload audio file and get URL from storage service
        String audioUrl = storageService.uploadFile(audio, "songs");

        // Upload cover image if provided and get URL from storage service
        String coverUrl = cover != null
                ? storageService.uploadFile(cover, "covers")
                : null;

        SongEntity song = SongEntity.builder()
                .title(title)
                .artist(artist)
                .album(album)
                .genre(genre)
                .duration(duration)
                .audioUrl(audioUrl)
                .coverImageUrl(coverUrl)
                .build();

        SongEntity savedSong = songService.saveSong(song);
        return new ResponseEntity<>(savedSong, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/played")
    public ResponseEntity<?> markAsPlayed(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        recentlyPlayedService.markAsPlayed(id, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recently-played")
    public ResponseEntity<List<SongResponse>> getRecentlyPlayed(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(recentlyPlayedService.getRecentlyPlayed(user));
    }

    @GetMapping("/played-count")
    public ResponseEntity<Long> getPlayedCount(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(recentlyPlayedService.getPlayedCount(user));
    }
}
