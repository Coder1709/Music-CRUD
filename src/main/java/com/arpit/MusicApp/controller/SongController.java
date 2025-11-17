package com.arpit.MusicApp.controller;

import com.arpit.MusicApp.dto.SongDto;
import com.arpit.MusicApp.entity.Song;
import com.arpit.MusicApp.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SongController {
    
    @Autowired
    private SongService songService;
    
    @GetMapping
    public ResponseEntity<List<SongDto>> getAllSongs() {
        List<SongDto> songs = songService.getAllSongs();
        return ResponseEntity.ok(songs);
    }
    
    /**
     * Get a specific song by ID
     * Returns 404 if song not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSongById(@PathVariable Long id) {
        SongDto song = songService.getSongById(id);
        return ResponseEntity.ok(song);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<SongDto>> searchSongs(@RequestParam String q) {
        List<SongDto> songs = songService.searchSongs(q);
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<SongDto>> getSongsByGenre(@PathVariable Song.Genre genre) {
        List<SongDto> songs = songService.getSongsByGenre(genre);
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/artist")
    public ResponseEntity<List<SongDto>> getSongsByArtist(@RequestParam String artist) {
        List<SongDto> songs = songService.getSongsByArtist(artist);
        return ResponseEntity.ok(songs);
    }
    
    /**
     * Add a new song (Admin only)
     * Requires ADMIN role
     */
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongDto> addSong(@RequestBody SongDto songDto) {
        SongDto savedSong = songService.addSong(songDto);
        return ResponseEntity.ok(savedSong);
    }
    
    /**
     * Upload song with audio file and optional cover image (Admin only)
     * Stores files as BLOBs in database
     * Requires ADMIN role
     */
    @PostMapping("/admin/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongDto> uploadSong(
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam("album") String album,
            @RequestParam("genre") Song.Genre genre,
            @RequestParam("duration") Integer duration,
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) {
        
        try {
            // Create song entity
            Song song = new Song();
            song.setTitle(title);
            song.setArtist(artist);
            song.setAlbum(album);
            song.setGenre(genre);
            song.setDuration(duration);
            
            // Store audio file as BLOB
            song.setAudioData(audioFile.getBytes());
            song.setAudioContentType(audioFile.getContentType());
            song.setAudioFilename(audioFile.getOriginalFilename());
            
            // Store cover image as BLOB if provided
            if (coverImage != null && !coverImage.isEmpty()) {
                song.setCoverImageData(coverImage.getBytes());
                song.setCoverImageContentType(coverImage.getContentType());
                song.setCoverImageFilename(coverImage.getOriginalFilename());
            }
            
            // Save song to database
            Song savedSong = songService.saveSongWithBlob(song);
            return ResponseEntity.ok(songService.convertToDto(savedSong));
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload song: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update an existing song (Admin only)
     * Requires ADMIN role
     */
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongDto> updateSong(@PathVariable Long id, @RequestBody SongDto songDto) {
        SongDto updatedSong = songService.updateSong(id, songDto);
        return ResponseEntity.ok(updatedSong);
    }
    
    /**
     * Delete a song (Admin only)
     * Requires ADMIN role
     */
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.ok("Song deleted successfully");
    }
}
