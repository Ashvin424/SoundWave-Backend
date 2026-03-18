package com.ashvinprajapati.soundwave.playlist.dto;

import com.ashvinprajapati.soundwave.song.dto.SongResponse;
import com.ashvinprajapati.soundwave.song.entity.SongEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlaylistResponse {
    private long id;
    private String name;
    private List<SongResponse> songs;
}
