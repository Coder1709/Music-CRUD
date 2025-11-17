package com.arpit.MusicApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "playback_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaybackSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_song_id")
    private Song currentSong;
    
    @Enumerated(EnumType.STRING)
    private PlaybackState state = PlaybackState.STOPPED;
    
    private Integer currentPosition = 0; // in seconds
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
    
    public enum PlaybackState {
        PLAYING, PAUSED, STOPPED
    }
}
