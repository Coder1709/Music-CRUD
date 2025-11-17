package com.arpit.MusicApp.service;

import com.arpit.MusicApp.dto.SongDto;
import com.arpit.MusicApp.entity.Song;
import com.arpit.MusicApp.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongService {
    
    @Autowired
    private SongRepository songRepository;
    
    public List<SongDto> getAllSongs() {
        return songRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public SongDto getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "Song", "id", id));
        return convertToDto(song);
    }
    
    public List<SongDto> searchSongs(String searchTerm) {
        return songRepository.searchSongs(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<SongDto> getSongsByGenre(Song.Genre genre) {
        return songRepository.findByGenre(genre).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<SongDto> getSongsByArtist(String artist) {
        return songRepository.findByArtistContainingIgnoreCase(artist).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public SongDto addSong(SongDto songDto) {
        Song song = new Song();
        song.setTitle(songDto.getTitle());
        song.setArtist(songDto.getArtist());
        song.setAlbum(songDto.getAlbum());
        song.setGenre(songDto.getGenre());
        song.setDuration(songDto.getDuration());
        song.setFilePath(songDto.getFilePath());
        song.setCoverImagePath(songDto.getCoverImagePath());
        
        Song savedSong = songRepository.save(song);
        return convertToDto(savedSong);
    }
    
    public SongDto updateSong(Long id, SongDto songDto) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "Song", "id", id));
        
        song.setTitle(songDto.getTitle());
        song.setArtist(songDto.getArtist());
        song.setAlbum(songDto.getAlbum());
        song.setGenre(songDto.getGenre());
        song.setDuration(songDto.getDuration());
        song.setFilePath(songDto.getFilePath());
        song.setCoverImagePath(songDto.getCoverImagePath());
        
        Song updatedSong = songRepository.save(song);
        return convertToDto(updatedSong);
    }
    
    public void deleteSong(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "Song", "id", id));
        songRepository.delete(song);
    }
    
    public Song saveSongWithBlob(Song song) {
        return songRepository.save(song);
    }
    
    public Song getSongEntityById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "Song", "id", id));
    }
    
    public SongDto convertToDto(Song song) {
        SongDto dto = new SongDto();
        dto.setId(song.getId());
        dto.setTitle(song.getTitle());
        dto.setArtist(song.getArtist());
        dto.setAlbum(song.getAlbum());
        dto.setGenre(song.getGenre());
        dto.setDuration(song.getDuration());
        dto.setFilePath(song.getFilePath());
        dto.setCoverImagePath(song.getCoverImagePath());
        dto.setAudioFilename(song.getAudioFilename());
        dto.setAudioContentType(song.getAudioContentType());
        dto.setCoverImageFilename(song.getCoverImageFilename());
        dto.setCoverImageContentType(song.getCoverImageContentType());
        dto.setHasAudioData(song.getAudioData() != null && song.getAudioData().length > 0);
        dto.setHasCoverImage(song.getCoverImageData() != null && song.getCoverImageData().length > 0);
        return dto;
    }
}
