package com.ashvinprajapati.soundwave.song.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    private String album;

    private String genre;

    @Column(nullable = false)
    private Long duration; // Duration in seconds

    @Column(nullable = false)
    private String audioUrl; // Path to the song file

    private String coverImageUrl; // Path to the cover image file

    @CreationTimestamp
    private LocalDateTime createdAt;
}
