package com.ashvinprajapati.soundwave.playlist.service;

import com.ashvinprajapati.soundwave.auth.entity.User;
import com.ashvinprajapati.soundwave.playlist.dto.PlaylistResponse;
import com.ashvinprajapati.soundwave.playlist.entity.PlaylistEntity;
import com.ashvinprajapati.soundwave.playlist.repository.PlaylistRepository;
import com.ashvinprajapati.soundwave.song.dto.SongResponse;
import com.ashvinprajapati.soundwave.song.entity.SongEntity;
import com.ashvinprajapati.soundwave.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    // Create a new playlist for a user
    public PlaylistResponse createPlaylist(String name, User user) {
        PlaylistEntity playlist = PlaylistEntity.builder()
                .name(name)
                .owner(user)
                .songs(new ArrayList<>())
                .build();

        playlist = playlistRepository.save(playlist);
        return mapToResponse(playlist);
    }



    public List<PlaylistResponse> getUserPlaylists(User user) {
        List<PlaylistEntity> playlist = playlistRepository.findByOwner(user);

        return playlist.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Additional methods for updating and deleting playlists can be added here
    public PlaylistEntity addSongToPlaylist(Long playlistId, Long songId, User user) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to modify this playlist");
        }

        if (playlist.getSongs().stream().anyMatch(song -> song.getId() == songId)) {
            throw new RuntimeException("Song already in playlist");
        }

        SongEntity song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        playlist.getSongs().add(song);

        return playlistRepository.save(playlist);
    }

    public PlaylistEntity removeSongFromPlaylist(Long playlistId, Long songId, User user) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to modify this playlist");
        }

        playlist.getSongs().removeIf(song -> song.getId() == songId);

        return playlistRepository.save(playlist);
    }

    private PlaylistResponse mapToResponse(PlaylistEntity playlist) {
        List<SongResponse> songResponses = playlist.getSongs()
                .stream()
                .map(songEntity -> SongResponse.builder()
                        .id(songEntity.getId())
                        .title(songEntity.getTitle())
                        .artist(songEntity.getArtist())
                        .audioUrl(songEntity.getAudioUrl())
                        .genre(songEntity.getGenre())
                        .album(songEntity.getAlbum())
                        .duration(songEntity.getDuration())
                        .coverImageUrl(songEntity.getCoverImageUrl())
                        .build())
                .toList();

        return PlaylistResponse.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .songs(songResponses)
                .build();
    }
}
