package com.ashvinprajapati.soundwave.playlist.controller;

import com.ashvinprajapati.soundwave.auth.entity.User;
import com.ashvinprajapati.soundwave.auth.repository.UserRepository;
import com.ashvinprajapati.soundwave.playlist.dto.PlaylistResponse;
import com.ashvinprajapati.soundwave.playlist.dto.UpdatePlaylistRequest;
import com.ashvinprajapati.soundwave.playlist.entity.PlaylistEntity;
import com.ashvinprajapati.soundwave.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createPlaylist(
            @RequestParam String name,
            @AuthenticationPrincipal User user
            ) {
        try {
            PlaylistResponse createdPlaylist = playlistService.createPlaylist(name, user);
            return new ResponseEntity<>(createdPlaylist, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<PlaylistResponse>> getUserPlaylists(
            @AuthenticationPrincipal User user
    ) {
        List<PlaylistResponse> playlists = playlistService.getUserPlaylists(user);
        return new ResponseEntity<>(playlists, HttpStatus.OK);
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<?> addSongToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId,
            @AuthenticationPrincipal User user
    ) {
        try {
            PlaylistEntity newPlaylist = playlistService.addSongToPlaylist(playlistId, songId, user);
            return new ResponseEntity<>(newPlaylist, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<?> removeSongFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId,
            @AuthenticationPrincipal User user
    ) {
        try {
            PlaylistEntity newPlaylist = playlistService.removeSongFromPlaylist(playlistId, songId, user);
            return new ResponseEntity<>(newPlaylist, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{playlistId}/delete")
    public ResponseEntity<?> deletePlaylist(
            @PathVariable Long playlistId,
            Authentication authentication
    ) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            playlistService.deletePlaylist(playlistId, user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{playlistId}/update")
    public ResponseEntity<?> updatePlaylist(
            @PathVariable Long playlistId,
            @RequestBody UpdatePlaylistRequest request,
            Authentication authentication
    ) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            PlaylistEntity updatedPlaylist = playlistService.editPlaylist(playlistId, request.getName(), user);
            return new ResponseEntity<>(updatedPlaylist, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
