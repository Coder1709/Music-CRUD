package com.arpit.MusicApp.repository;

import com.arpit.MusicApp.entity.Playlist;
import com.arpit.MusicApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUser(User user);
    List<Playlist> findByUserOrderByCreatedAtDesc(User user);
}
