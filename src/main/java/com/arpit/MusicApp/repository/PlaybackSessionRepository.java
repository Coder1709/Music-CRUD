package com.arpit.MusicApp.repository;

import com.arpit.MusicApp.entity.PlaybackSession;
import com.arpit.MusicApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaybackSessionRepository extends JpaRepository<PlaybackSession, Long> {
    Optional<PlaybackSession> findByUser(User user);
}
