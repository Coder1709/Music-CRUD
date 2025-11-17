package com.arpit.MusicApp.service;

import com.arpit.MusicApp.dto.PlaybackSessionDto;
import com.arpit.MusicApp.dto.SongDto;
import com.arpit.MusicApp.entity.PlaybackSession;
import com.arpit.MusicApp.entity.Song;
import com.arpit.MusicApp.entity.User;
import com.arpit.MusicApp.repository.PlaybackSessionRepository;
import com.arpit.MusicApp.repository.SongRepository;
import com.arpit.MusicApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaybackService {
    
    @Autowired
    private PlaybackSessionRepository playbackSessionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SongRepository songRepository;
    
    public PlaybackSessionDto playSong(String username, Long songId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "User", "username", username));
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "Song", "id", songId));
        
        PlaybackSession session = playbackSessionRepository.findByUser(user)
                .orElse(new PlaybackSession());
        
        session.setUser(user);
        session.setCurrentSong(song);
        session.setState(PlaybackSession.PlaybackState.PLAYING);
        session.setCurrentPosition(0);
        
        PlaybackSession savedSession = playbackSessionRepository.save(session);
        return convertToDto(savedSession);
    }
    
    public PlaybackSessionDto pausePlayback(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "User", "username", username));
        
        PlaybackSession session = playbackSessionRepository.findByUser(user)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "No active playback session found"));
        
        session.setState(PlaybackSession.PlaybackState.PAUSED);
        PlaybackSession savedSession = playbackSessionRepository.save(session);
        return convertToDto(savedSession);
    }
    
    public PlaybackSessionDto resumePlayback(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "User", "username", username));
        
        PlaybackSession session = playbackSessionRepository.findByUser(user)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "No active playback session found"));
        
        session.setState(PlaybackSession.PlaybackState.PLAYING);
        PlaybackSession savedSession = playbackSessionRepository.save(session);
        return convertToDto(savedSession);
    }
    
    public PlaybackSessionDto stopPlayback(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "User", "username", username));
        
        PlaybackSession session = playbackSessionRepository.findByUser(user)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "No active playback session found"));
        
        session.setState(PlaybackSession.PlaybackState.STOPPED);
        session.setCurrentPosition(0);
        PlaybackSession savedSession = playbackSessionRepository.save(session);
        return convertToDto(savedSession);
    }
    
    public PlaybackSessionDto getCurrentPlayback(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "User", "username", username));
        
        PlaybackSession session = playbackSessionRepository.findByUser(user)
                .orElse(null);
        
        if (session == null) {
            return new PlaybackSessionDto();
        }
        
        return convertToDto(session);
    }
    
    public PlaybackSessionDto updatePosition(String username, Integer position) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "User", "username", username));
        
        PlaybackSession session = playbackSessionRepository.findByUser(user)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "No active playback session found"));
        
        session.setCurrentPosition(position);
        PlaybackSession savedSession = playbackSessionRepository.save(session);
        return convertToDto(savedSession);
    }
    
    private PlaybackSessionDto convertToDto(PlaybackSession session) {
        PlaybackSessionDto dto = new PlaybackSessionDto();
        dto.setId(session.getId());
        dto.setState(session.getState());
        dto.setCurrentPosition(session.getCurrentPosition());
        
        if (session.getCurrentSong() != null) {
            SongDto songDto = new SongDto();
            songDto.setId(session.getCurrentSong().getId());
            songDto.setTitle(session.getCurrentSong().getTitle());
            songDto.setArtist(session.getCurrentSong().getArtist());
            songDto.setAlbum(session.getCurrentSong().getAlbum());
            songDto.setGenre(session.getCurrentSong().getGenre());
            songDto.setDuration(session.getCurrentSong().getDuration());
            songDto.setFilePath(session.getCurrentSong().getFilePath());
            songDto.setCoverImagePath(session.getCurrentSong().getCoverImagePath());
            dto.setCurrentSong(songDto);
        }
        
        return dto;
    }
}
