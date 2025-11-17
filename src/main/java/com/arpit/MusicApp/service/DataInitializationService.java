package com.arpit.MusicApp.service;

import com.arpit.MusicApp.entity.Song;
import com.arpit.MusicApp.entity.User;
import com.arpit.MusicApp.repository.SongRepository;
import com.arpit.MusicApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SongRepository songRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initializeData();
    }
    
    private void initializeData() {
        // Create admin user if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@musicapp.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
        }
        
        // Create regular user if not exists
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setEmail("user@musicapp.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFirstName("Regular");
            user.setLastName("User");
            user.setRole(User.Role.USER);
            userRepository.save(user);
        }
        
        // Add sample songs if not exists
        if (songRepository.count() == 0) {
            createSampleSongs();
        }
    }
    
    private void createSampleSongs() {
        // Create sample songs with metadata only
        // Audio files and cover images should be uploaded by admins using the upload endpoint
        
        Song song1 = new Song();
        song1.setTitle("Bohemian Rhapsody");
        song1.setArtist("Queen");
        song1.setAlbum("A Night at the Opera");
        song1.setGenre(Song.Genre.ROCK);
        song1.setDuration(355);
        songRepository.save(song1);
        
        Song song2 = new Song();
        song2.setTitle("Hotel California");
        song2.setArtist("Eagles");
        song2.setAlbum("Hotel California");
        song2.setGenre(Song.Genre.ROCK);
        song2.setDuration(391);
        songRepository.save(song2);
        
        Song song3 = new Song();
        song3.setTitle("Billie Jean");
        song3.setArtist("Michael Jackson");
        song3.setAlbum("Thriller");
        song3.setGenre(Song.Genre.POP);
        song3.setDuration(294);
        songRepository.save(song3);
        
        Song song4 = new Song();
        song4.setTitle("Imagine");
        song4.setArtist("John Lennon");
        song4.setAlbum("Imagine");
        song4.setGenre(Song.Genre.POP);
        song4.setDuration(183);
        songRepository.save(song4);
        
        Song song5 = new Song();
        song5.setTitle("What's Going On");
        song5.setArtist("Marvin Gaye");
        song5.setAlbum("What's Going On");
        song5.setGenre(Song.Genre.POP);
        song5.setDuration(231);
        songRepository.save(song5);
        
        Song song6 = new Song();
        song6.setTitle("Stairway to Heaven");
        song6.setArtist("Led Zeppelin");
        song6.setAlbum("Led Zeppelin IV");
        song6.setGenre(Song.Genre.ROCK);
        song6.setDuration(482);
        songRepository.save(song6);
        
        Song song7 = new Song();
        song7.setTitle("Like a Rolling Stone");
        song7.setArtist("Bob Dylan");
        song7.setAlbum("Highway 61 Revisited");
        song7.setGenre(Song.Genre.ROCK);
        song7.setDuration(369);
        songRepository.save(song7);
        
        Song song8 = new Song();
        song8.setTitle("Smells Like Teen Spirit");
        song8.setArtist("Nirvana");
        song8.setAlbum("Nevermind");
        song8.setGenre(Song.Genre.ROCK);
        song8.setDuration(301);
        songRepository.save(song8);
        
        Song song9 = new Song();
        song9.setTitle("What'd I Say");
        song9.setArtist("Ray Charles");
        song9.setAlbum("The Genius of Ray Charles");
        song9.setGenre(Song.Genre.POP);
        song9.setDuration(393);
        songRepository.save(song9);
        
        Song song10 = new Song();
        song10.setTitle("Yesterday");
        song10.setArtist("The Beatles");
        song10.setAlbum("Help!");
        song10.setGenre(Song.Genre.POP);
        song10.setDuration(125);
        songRepository.save(song10);
        
        System.out.println("✅ Created 10 sample songs. Use the admin upload endpoint to add audio files.");
    }
}
