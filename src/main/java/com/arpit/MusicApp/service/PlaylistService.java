package com.arpit.MusicApp.service;

import com.arpit.MusicApp.dto.PlaylistDto;
import com.arpit.MusicApp.dto.SongDto;
import com.arpit.MusicApp.entity.Playlist;
import com.arpit.MusicApp.entity.Song;
import com.arpit.MusicApp.entity.User;
import com.arpit.MusicApp.repository.PlaylistRepository;
import com.arpit.MusicApp.repository.SongRepository;
import com.arpit.MusicApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    
    @Autowired
    private PlaylistRepository playlistRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SongRepository songRepository;
    
    public PlaylistDto createPlaylist(String username, String name, String description) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "User", "username", username));
        
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setUser(user);
        playlist.setSongs(new ArrayList<>());
        
        Playlist savedPlaylist = playlistRepository.save(playlist);
        return convertToDto(savedPlaylist);
    }
    
    public List<PlaylistDto> getUserPlaylists(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "User", "username", username));
        
        return playlistRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public PlaylistDto getPlaylistById(Long id, String username) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "Playlist", "id", id));
        
        // Check if the playlist belongs to the requesting user
        if (!playlist.getUser().getUsername().equals(username)) {
            throw new com.arpit.MusicApp.exception.UnauthorizedAccessException(
                "You don't have permission to access this playlist");
        }
        
        return convertToDto(playlist);
    }
    
    public PlaylistDto addSongToPlaylist(Long playlistId, Long songId, String username) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "Playlist", "id", playlistId));
        
        // Verify ownership before allowing modification
        if (!playlist.getUser().getUsername().equals(username)) {
            throw new com.arpit.MusicApp.exception.UnauthorizedAccessException(
                "You don't have permission to modify this playlist");
        }
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "Song", "id", songId));
        
        if (!playlist.getSongs().contains(song)) {
            playlist.getSongs().add(song);
            playlistRepository.save(playlist);
        }
        
        return convertToDto(playlist);
    }
    
    public PlaylistDto removeSongFromPlaylist(Long playlistId, Long songId, String username) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "Playlist", "id", playlistId));
        
        // Verify ownership before allowing modification
        if (!playlist.getUser().getUsername().equals(username)) {
            throw new com.arpit.MusicApp.exception.UnauthorizedAccessException(
                "You don't have permission to modify this playlist");
        }
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "Song", "id", songId));
        
        playlist.getSongs().remove(song);
        playlistRepository.save(playlist);
        
        return convertToDto(playlist);
    }
    
    public void deletePlaylist(Long id, String username) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "Playlist", "id", id));
        
        // Verify ownership before allowing deletion
        if (!playlist.getUser().getUsername().equals(username)) {
            throw new com.arpit.MusicApp.exception.UnauthorizedAccessException(
                "You don't have permission to delete this playlist");
        }
        
        playlistRepository.delete(playlist);
    }
    
    private PlaylistDto convertToDto(Playlist playlist) {
        PlaylistDto dto = new PlaylistDto();
        dto.setId(playlist.getId());
        dto.setName(playlist.getName());
        dto.setDescription(playlist.getDescription());
        dto.setUsername(playlist.getUser().getUsername());
        dto.setSongCount(playlist.getSongs() != null ? playlist.getSongs().size() : 0);
        
        if (playlist.getSongs() != null) {
            dto.setSongs(playlist.getSongs().stream()
                    .map(this::convertSongToDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    private SongDto convertSongToDto(Song song) {
        SongDto dto = new SongDto();
        dto.setId(song.getId());
        dto.setTitle(song.getTitle());
        dto.setArtist(song.getArtist());
        dto.setAlbum(song.getAlbum());
        dto.setGenre(song.getGenre());
        dto.setDuration(song.getDuration());
        dto.setFilePath(song.getFilePath());
        dto.setCoverImagePath(song.getCoverImagePath());
        return dto;
    }
}
