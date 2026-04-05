package com.ashvinprajapati.soundwave.song.service;

import com.ashvinprajapati.soundwave.song.dto.SongResponse;
import com.ashvinprajapati.soundwave.song.dto.SongsPageResponse;
import com.ashvinprajapati.soundwave.song.entity.SongEntity;
import com.ashvinprajapati.soundwave.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    public SongEntity saveSong(SongEntity song) {
        return songRepository.save(song);
    }

    public List<SongEntity> getAllSongs() {
        return songRepository.findAll();
    }

    public SongsPageResponse searchSongs(
            String keyword,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {

        List<String> allowedSortFields = List.of("title", "artist", "createdAt");

        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "title";
        }

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SongEntity> songPage = songRepository.searchSongs(keyword, pageable);

        List<SongResponse> songs = songPage.getContent()
                .stream()
                .map(song -> SongResponse.builder()
                        .id(song.getId())
                        .title(song.getTitle())
                        .artist(song.getArtist())
                        .audioUrl(song.getAudioUrl())
                        .duration(song.getDuration())
                        .genre(song.getGenre())
                        .album(song.getAlbum())
                        .coverImageUrl(song.getCoverImageUrl())
                        .build())
                .toList();

        return SongsPageResponse.builder()
                .content(songs)
                .page(songPage.getNumber())
                .size(songPage.getSize())
                .totalElements(songPage.getTotalElements())
                .totalPages(songPage.getTotalPages())
                .last(songPage.isLast())
                .build();
    }

    public List<SongEntity> searchByTitle(String title) {
        return songRepository.findByTitleContainingIgnoreCase(title);
    }
}
