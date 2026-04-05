package com.ashvinprajapati.soundwave.song.entity;

import com.ashvinprajapati.soundwave.auth.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recent_played")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentPlayed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private SongEntity song;

    @Column(nullable = false)
    private LocalDateTime playedAt;
}
