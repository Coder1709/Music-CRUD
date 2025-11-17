package com.arpit.MusicApp.controller;

import com.arpit.MusicApp.dto.PlaylistDto;
import com.arpit.MusicApp.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlaylistController {
    
    @Autowired
    private PlaylistService playlistService;
    
    /**
     * Create a new playlist for the authenticated user
     * User is extracted from security context
     */
    @PostMapping
    public ResponseEntity<PlaylistDto> createPlaylist(@RequestBody Map<String, String> request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String name = request.get("name");
        String description = request.get("description");
        
        PlaylistDto playlist = playlistService.createPlaylist(username, name, description);
        return ResponseEntity.ok(playlist);
    }
    
    @GetMapping
    public ResponseEntity<List<PlaylistDto>> getUserPlaylists() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PlaylistDto> playlists = playlistService.getUserPlaylists(username);
        return ResponseEntity.ok(playlists);
    }
    
    /**
     * Get a specific playlist by ID
     * Verifies user owns the playlist
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDto> getPlaylistById(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PlaylistDto playlist = playlistService.getPlaylistById(id, username);
        return ResponseEntity.ok(playlist);
    }
    
    /**
     * Add a song to a playlist
     * Verifies user owns the playlist
     */
    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<PlaylistDto> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PlaylistDto playlist = playlistService.addSongToPlaylist(playlistId, songId, username);
        return ResponseEntity.ok(playlist);
    }
    
    /**
     * Remove a song from a playlist
     * Verifies user owns the playlist
     */
    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<PlaylistDto> removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PlaylistDto playlist = playlistService.removeSongFromPlaylist(playlistId, songId, username);
        return ResponseEntity.ok(playlist);
    }
    
    /**
     * Delete a playlist
     * Verifies user owns the playlist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        playlistService.deletePlaylist(id, username);
        return ResponseEntity.ok("Playlist deleted successfully");
    }
}
