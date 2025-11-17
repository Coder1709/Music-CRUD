package com.arpit.MusicApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String artist;
    
    private String album;
    
    @Enumerated(EnumType.STRING)
    private Genre genre;
    
    private Integer duration; // in seconds
    
    private String filePath; // path to audio file (deprecated, kept for backward compatibility)
    
    private String coverImagePath; // path to cover image (deprecated, kept for backward compatibility)
    
    // Store actual audio file data
    @Lob
    @Column(name = "audio_data", columnDefinition = "BLOB")
    private byte[] audioData;
    
    @Column(name = "audio_content_type")
    private String audioContentType; // e.g., "audio/mpeg", "audio/wav"
    
    @Column(name = "audio_filename")
    private String audioFilename; // original filename
    
    // Store actual cover image data
    @Lob
    @Column(name = "cover_image_data", columnDefinition = "BLOB")
    private byte[] coverImageData;
    
    @Column(name = "cover_image_content_type")
    private String coverImageContentType; // e.g., "image/jpeg", "image/png"
    
    @Column(name = "cover_image_filename")
    private String coverImageFilename; // original filename
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToMany(mappedBy = "songs")
    private List<Playlist> playlists;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum Genre {
        POP, ROCK, JAZZ, CLASSICAL, HIP_HOP, ELECTRONIC, COUNTRY, R_AND_B, REGGAE, BLUES, FOLK, METAL, PUNK, ALTERNATIVE, INDIE, WORLD, OTHER
    }
}
