package com.ashvinprajapati.soundwave.song.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SongsPageResponse {
    private List<SongResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
