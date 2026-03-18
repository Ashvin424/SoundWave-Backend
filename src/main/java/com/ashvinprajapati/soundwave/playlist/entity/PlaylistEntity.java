package com.ashvinprajapati.soundwave.playlist.entity;

import com.ashvinprajapati.soundwave.auth.entity.User;
import com.ashvinprajapati.soundwave.song.entity.SongEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "playlists")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( // Join table to manage the many-to-many relationship between playlists and songs
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"), // Foreign key to the playlist
            inverseJoinColumns = @JoinColumn(name = "song_id") // Foreign key to the song
    )
    private List<SongEntity> songs;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
