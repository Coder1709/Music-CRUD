package com.arpit.MusicApp.repository;

import com.arpit.MusicApp.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByTitleContainingIgnoreCase(String title);
    List<Song> findByArtistContainingIgnoreCase(String artist);
    List<Song> findByGenre(Song.Genre genre);
    List<Song> findByAlbumContainingIgnoreCase(String album);
    
    @Query("SELECT s FROM Song s WHERE " +
           "LOWER(s.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.artist) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.album) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Song> searchSongs(@Param("searchTerm") String searchTerm);
}
