package com.ashvinprajapati.soundwave.song.repository;


import com.ashvinprajapati.soundwave.song.entity.SongEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongRepository extends JpaRepository<SongEntity, Long> {

    List<SongEntity> findByTitleContainingIgnoreCase(String title);
    List<SongEntity> findByArtistContainingIgnoreCase(String artist);

    @Query("""
        SELECT s FROM SongEntity s
        WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.artist) LIKE LOWER(CONCAT('%', :keyword, '%'))
       """)
    Page<SongEntity> searchSongs(@Param("keyword") String keyword, Pageable pageable);
}
