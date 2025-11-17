package com.arpit.MusicApp.dto;

import com.arpit.MusicApp.entity.PlaybackSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaybackSessionDto {
    private Long id;
    private SongDto currentSong;
    private PlaybackSession.PlaybackState state;
    private Integer currentPosition;
}
