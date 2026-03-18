package com.ashvinprajapati.soundwave.song.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongResponse {
    private long id;
    private String title;
    private String artist;
    private String audioUrl;
    private String coverImageUrl;
    private Long duration;
    private String album;
    private String genre;
}
