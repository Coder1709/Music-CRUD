package com.arpit.MusicApp.controller;

import com.arpit.MusicApp.dto.PlaybackSessionDto;
import com.arpit.MusicApp.service.PlaybackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/playback")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlaybackController {
    
    @Autowired
    private PlaybackService playbackService;
    
    /**
     * Start playing a song
     * Creates or updates playback session for the user
     */
    @PostMapping("/play/{songId}")
    public ResponseEntity<PlaybackSessionDto> playSong(@PathVariable Long songId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PlaybackSessionDto session = playbackService.playSong(username, songId);
        return ResponseEntity.ok(session);
    }
    
    /**
     * Pause current playback
     * Requires an active playback session
     */
    @PostMapping("/pause")
    public ResponseEntity<PlaybackSessionDto> pausePlayback() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PlaybackSessionDto session = playbackService.pausePlayback(username);
        return ResponseEntity.ok(session);
    }
    
    /**
     * Resume paused playback
     * Requires an active playback session
     */
    @PostMapping("/resume")
    public ResponseEntity<PlaybackSessionDto> resumePlayback() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PlaybackSessionDto session = playbackService.resumePlayback(username);
        return ResponseEntity.ok(session);
    }
    
    /**
     * Stop current playback
     * Resets position to 0
     */
    @PostMapping("/stop")
    public ResponseEntity<PlaybackSessionDto> stopPlayback() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PlaybackSessionDto session = playbackService.stopPlayback(username);
        return ResponseEntity.ok(session);
    }
    
    @GetMapping("/current")
    public ResponseEntity<PlaybackSessionDto> getCurrentPlayback() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PlaybackSessionDto session = playbackService.getCurrentPlayback(username);
        return ResponseEntity.ok(session);
    }
    
    /**
     * Update playback position
     * Position is in seconds
     */
    @PostMapping("/position")
    public ResponseEntity<PlaybackSessionDto> updatePosition(@RequestBody Map<String, Integer> request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer position = request.get("position");
        PlaybackSessionDto session = playbackService.updatePosition(username, position);
        return ResponseEntity.ok(session);
    }
}
