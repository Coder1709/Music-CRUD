package com.arpit.MusicApp.controller;

import com.arpit.MusicApp.entity.Song;
import com.arpit.MusicApp.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileController {

    @Autowired
    private SongService songService;

    /**
     * Stream audio file from database BLOB
     * This endpoint serves audio files for playback
     */
    @GetMapping("/audio/{songId}")
    public ResponseEntity<byte[]> streamAudio(@PathVariable Long songId) {
        try {
            Song song = songService.getSongEntityById(songId);
            
            if (song.getAudioData() == null || song.getAudioData().length == 0) {
                return ResponseEntity.notFound().build();
            }

            String contentType = song.getAudioContentType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + song.getAudioFilename() + "\"")
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                    .body(song.getAudioData());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Stream cover image from database BLOB
     * This endpoint serves cover images
     */
    @GetMapping("/cover/{songId}")
    public ResponseEntity<byte[]> streamCover(@PathVariable Long songId) {
        try {
            Song song = songService.getSongEntityById(songId);
            
            if (song.getCoverImageData() == null || song.getCoverImageData().length == 0) {
                return ResponseEntity.notFound().build();
            }

            String contentType = song.getCoverImageContentType();
            if (contentType == null) {
                contentType = "image/jpeg";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + song.getCoverImageFilename() + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .body(song.getCoverImageData());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
