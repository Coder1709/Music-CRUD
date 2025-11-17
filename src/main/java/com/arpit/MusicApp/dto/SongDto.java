package com.arpit.MusicApp.dto;

import com.arpit.MusicApp.entity.Song;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {
    private Long id;
    private String title;
    private String artist;
    private String album;
    private Song.Genre genre;
    private Integer duration;
    private String filePath; // deprecated
    private String coverImagePath; // deprecated
    private String audioFilename;
    private String audioContentType;
    private String coverImageFilename;
    private String coverImageContentType;
    private boolean hasAudioData;
    private boolean hasCoverImage;
}
