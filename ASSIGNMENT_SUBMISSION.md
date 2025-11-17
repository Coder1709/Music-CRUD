# 🎵 Music Streaming App - Assignment Submission

## 📋 Project Overview

This is a complete backend implementation for a music streaming application built using Java Spring Boot with PostgreSQL database support. The application provides all the requested features with additional enhancements for a production-ready system.

## ✅ Requirements Compliance

### Core Features Implemented:

1. **👤 User Management**
   - ✅ User Registration with validation
   - ✅ User Login with JWT authentication
   - ✅ User Profile viewing
   - ✅ Role-based access (USER/ADMIN)

2. **🎵 Song Management**
   - ✅ List all songs
   - ✅ Search songs by title, artist, and genre
   - ✅ Admin-only song management (Add/Edit/Delete)
   - ✅ Pre-loaded with 10 sample songs

3. **📁 Playlist Management**
   - ✅ Create personal playlists
   - ✅ Add/remove songs from playlists
   - ✅ View user's playlists
   - ✅ Playlist ownership security

4. **▶️ Playback Simulation**
   - ✅ Play songs with session tracking
   - ✅ Pause/Resume functionality
   - ✅ Stop playback
   - ✅ Show current playing song for user
   - ✅ Position tracking

### Tech Stack Requirements:
- ✅ **Java 17** (Compatible with Java 8+)
- ✅ **Spring Boot 3.5.7**
- ✅ **PostgreSQL** + H2 for testing
- ✅ **REST APIs** - Complete RESTful implementation
- ✅ **JPA/Hibernate** - Full ORM implementation
- ✅ **Maven** - Project management

## 🚀 Additional Features

### Security & Authentication:
- JWT-based authentication
- Password encryption with BCrypt
- Role-based authorization
- Secure API endpoints

### Database Features:
- Dual database support (PostgreSQL/H2)
- Automatic schema generation
- Sample data initialization
- Optimized queries with JPA

### Developer Experience:
- Comprehensive API documentation
- Postman collection for testing
- Automated test script
- H2 console for database inspection
- Detailed error handling

## 📁 Project Structure

```
MusicApp/
├── src/main/java/com/arpit/MusicApp/
│   ├── controller/          # REST API Controllers
│   │   ├── AuthController.java
│   │   ├── SongController.java
│   │   ├── PlaylistController.java
│   │   └── PlaybackController.java
│   ├── service/            # Business Logic Services
│   │   ├── AuthService.java
│   │   ├── SongService.java
│   │   ├── PlaylistService.java
│   │   ├── PlaybackService.java
│   │   └── DataInitializationService.java
│   ├── repository/         # Data Access Layer
│   │   ├── UserRepository.java
│   │   ├── SongRepository.java
│   │   ├── PlaylistRepository.java
│   │   └── PlaybackSessionRepository.java
│   ├── entity/            # JPA Entities
│   │   ├── User.java
│   │   ├── Song.java
│   │   ├── Playlist.java
│   │   └── PlaybackSession.java
│   ├── dto/               # Data Transfer Objects
│   │   ├── UserRegistrationDto.java
│   │   ├── LoginDto.java
│   │   ├── JwtResponseDto.java
│   │   ├── SongDto.java
│   │   ├── PlaylistDto.java
│   │   └── PlaybackSessionDto.java
│   ├── config/            # Configuration
│   │   └── SecurityConfig.java
│   ├── security/          # Security Components
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtAuthenticationEntryPoint.java
│   └── util/              # Utility Classes
│       └── JwtUtil.java
├── src/main/resources/
│   ├── application.properties          # PostgreSQL config
│   └── application-h2.properties       # H2 config for testing
├── test-api.sh                        # Automated API testing
├── MusicApp-Postman-Collection.json   # Postman collection
├── README.md                          # Complete documentation
└── pom.xml                           # Maven dependencies
```

## 🎯 API Endpoints Summary

### Authentication Endpoints:
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/auth/profile` - Get user profile

### Song Management:
- `GET /api/songs` - List all songs
- `GET /api/songs/{id}` - Get song by ID
- `GET /api/songs/search?q={term}` - Search songs
- `GET /api/songs/genre/{genre}` - Get songs by genre
- `GET /api/songs/artist?artist={name}` - Get songs by artist
- `POST /api/songs/admin` - Add song (Admin only)
- `PUT /api/songs/admin/{id}` - Update song (Admin only)
- `DELETE /api/songs/admin/{id}` - Delete song (Admin only)

### Playlist Management:
- `POST /api/playlists` - Create playlist
- `GET /api/playlists` - Get user playlists
- `GET /api/playlists/{id}` - Get playlist by ID
- `POST /api/playlists/{playlistId}/songs/{songId}` - Add song to playlist
- `DELETE /api/playlists/{playlistId}/songs/{songId}` - Remove song
- `DELETE /api/playlists/{id}` - Delete playlist

### Playback Control:
- `POST /api/playback/play/{songId}` - Play song
- `POST /api/playback/pause` - Pause playback
- `POST /api/playback/resume` - Resume playback
- `POST /api/playback/stop` - Stop playback
- `GET /api/playback/current` - Get current playback
- `POST /api/playback/position` - Update position

## 🔧 How to Run

### Quick Start (H2 Database):
```bash
# Build the project
mvn clean package

# Run with H2 database for testing
java -jar target/MusicApp-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2

# Application will be available at: http://localhost:8080
```

### Production Setup (PostgreSQL):
```bash
# Create PostgreSQL database
createdb musicapp

# Update credentials in application.properties
# Run the application
java -jar target/MusicApp-0.0.1-SNAPSHOT.jar
```

## 🧪 Testing

### Default User Accounts:
- **Admin**: username: `admin`, password: `admin123`
- **User**: username: `user`, password: `user123`

### Testing Options:
1. **Automated Script**: `./test-api.sh`
2. **Postman Collection**: Import `MusicApp-Postman-Collection.json`
3. **H2 Console**: http://localhost:8080/h2-console
4. **Manual cURL**: See examples in README.md

## 📊 Sample Data

The application includes 10 pre-loaded songs covering various genres:
- Rock: Queen, Eagles, Led Zeppelin, Nirvana
- Pop: Michael Jackson, John Lennon, The Beatles
- R&B: Marvin Gaye, Ray Charles
- Folk: Bob Dylan

## 🎉 Success Metrics

- ✅ All core requirements implemented
- ✅ Additional security features added
- ✅ Production-ready code structure
- ✅ Comprehensive documentation
- ✅ Multiple testing approaches
- ✅ Database flexibility (PostgreSQL/H2)
- ✅ Clean, maintainable code

## 📸 Screenshots

To generate screenshots for submission:

1. **H2 Console**: Navigate to http://localhost:8080/h2-console
2. **API Testing**: Run `./test-api.sh` and capture terminal output
3. **Postman**: Import collection and test endpoints
4. **Database Structure**: View tables in H2 console

## 🏆 Conclusion

This Music Streaming App backend provides a robust, scalable foundation for a music streaming service. It exceeds the basic requirements by including advanced features like JWT authentication, role-based security, comprehensive search capabilities, and extensive testing support.

The codebase follows Spring Boot best practices, includes proper error handling, and is ready for production deployment with PostgreSQL or development/testing with H2 database.
