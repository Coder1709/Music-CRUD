#  Music Streaming App Backend

A comprehensive backend system for a music streaming application built with Java Spring Boot and PostgreSQL.

##  Features

- ** User Management**: Registration, login, and profile management
- ** Song Management**: Add, search, and manage songs (admin functionality)
- * Playlist Management**: Create playlists, add/remove songs
-  Playback Simulation**: Play, pause, resume, stop functionality
- ** JWT Authentication**: Secure API endpoints
- ** Role-based Access Control**: Admin and User roles
- ** Advanced Search**: Search songs by title, artist, album, or genre
- ** Sample Data**: Pre-loaded with 10 popular songs for testing

##  Tech Stack

- **Backend**: Java 17, Spring Boot 3.5.7
- **Security**: Spring Security, JWT Authentication
- **Database**: PostgreSQL (Production), H2 (Testing)
- **ORM**: Spring Data JPA, Hibernate
- **Build Tool**: Maven
- **Libraries**: Lombok, JJWT

## Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

## Database Setup

1. Install PostgreSQL
2. Create a database named `musicapp`
3. Update database credentials in `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/musicapp
spring.datasource.username=postgres
spring.datasource.password=password
```

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Default Users

The application creates two default users:
- **Admin**: username: `admin`, password: `admin123`
- **User**: username: `user`, password: `user123`

## API Endpoints

### Authentication

#### Register User
- **POST** `/api/auth/register`
- **Body**:
```json
{
  "username": "newuser",
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Login
- **POST** `/api/auth/login`
- **Body**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```
- **Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "admin",
  "email": "admin@musicapp.com",
  "role": "ADMIN"
}
```

#### Get Profile
- **GET** `/api/auth/profile`
- **Headers**: `Authorization: Bearer <token>`

### Songs

#### Get All Songs
- **GET** `/api/songs`
- **Headers**: `Authorization: Bearer <token>`

#### Get Song by ID
- **GET** `/api/songs/{id}`
- **Headers**: `Authorization: Bearer <token>`

#### Search Songs
- **GET** `/api/songs/search?q=bohemian`
- **Headers**: `Authorization: Bearer <token>`

#### Get Songs by Genre
- **GET** `/api/songs/genre/ROCK`
- **Headers**: `Authorization: Bearer <token>`

#### Get Songs by Artist
- **GET** `/api/songs/artist?artist=Queen`
- **Headers**: `Authorization: Bearer <token>`

#### Add Song (Admin Only)
- **POST** `/api/songs/admin`
- **Headers**: `Authorization: Bearer <token>`
- **Body**:
```json
{
  "title": "New Song",
  "artist": "Artist Name",
  "album": "Album Name",
  "genre": "POP",
  "duration": 240,
  "filePath": "/songs/new-song.mp3",
  "coverImagePath": "/covers/new-song.jpg"
}
```

#### Update Song (Admin Only)
- **PUT** `/api/songs/admin/{id}`
- **Headers**: `Authorization: Bearer <token>`

#### Delete Song (Admin Only)
- **DELETE** `/api/songs/admin/{id}`
- **Headers**: `Authorization: Bearer <token>`

### Playlists

#### Create Playlist
- **POST** `/api/playlists`
- **Headers**: `Authorization: Bearer <token>`
- **Body**:
```json
{
  "name": "My Playlist",
  "description": "My favorite songs"
}
```

#### Get User Playlists
- **GET** `/api/playlists`
- **Headers**: `Authorization: Bearer <token>`

#### Get Playlist by ID
- **GET** `/api/playlists/{id}`
- **Headers**: `Authorization: Bearer <token>`

#### Add Song to Playlist
- **POST** `/api/playlists/{playlistId}/songs/{songId}`
- **Headers**: `Authorization: Bearer <token>`

#### Remove Song from Playlist
- **DELETE** `/api/playlists/{playlistId}/songs/{songId}`
- **Headers**: `Authorization: Bearer <token>`

#### Delete Playlist
- **DELETE** `/api/playlists/{id}`
- **Headers**: `Authorization: Bearer <token>`

### Playback

#### Play Song
- **POST** `/api/playback/play/{songId}`
- **Headers**: `Authorization: Bearer <token>`

#### Pause Playback
- **POST** `/api/playback/pause`
- **Headers**: `Authorization: Bearer <token>`

#### Resume Playback
- **POST** `/api/playback/resume`
- **Headers**: `Authorization: Bearer <token>`

#### Stop Playback
- **POST** `/api/playback/stop`
- **Headers**: `Authorization: Bearer <token>`

#### Get Current Playback
- **GET** `/api/playback/current`
- **Headers**: `Authorization: Bearer <token>`

#### Update Position
- **POST** `/api/playback/position`
- **Headers**: `Authorization: Bearer <token>`
- **Body**:
```json
{
  "position": 120
}
```

## Available Genres

- POP
- ROCK
- JAZZ
- CLASSICAL
- HIP_HOP
- ELECTRONIC
- COUNTRY
- R_AND_B
- REGGAE
- BLUES
- FOLK
- METAL
- PUNK
- ALTERNATIVE
- INDIE
- WORLD
- OTHER

## Testing with Postman/cURL

1. First, login to get a JWT token
2. Use the token in the Authorization header for protected endpoints
3. Admin endpoints require admin role

Example cURL commands:

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Get all songs
curl -X GET http://localhost:8080/api/songs \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Play a song
curl -X POST http://localhost:8080/api/playback/play/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Database Schema

The application uses the following main entities:
- **Users**: User accounts with roles
- **Songs**: Music tracks with metadata
- **Playlists**: User-created song collections
- **PlaybackSessions**: Current playback state per user

## Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based access control
- CORS enabled for frontend integration

##  Quick Start

### Running with H2 Database (for testing)
```bash
# Clone and build
git clone <repository-url>
cd MusicApp
mvn clean package

# Run with H2 database
java -jar target/MusicApp-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2
```

### Running with PostgreSQL (production)
```bash
# Setup PostgreSQL database
createdb musicapp

# Run with default profile
java -jar target/MusicApp-0.0.1-SNAPSHOT.jar
```

##  Screenshots & Testing

The application provides several ways to test and interact with the APIs:

### 1. H2 Database Console
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

### 2. API Testing Script
Run the comprehensive test script:
```bash
chmod +x test-api.sh
./test-api.sh
```

### 3. Postman Collection
Import the provided Postman collection: `MusicApp-Postman-Collection.json`

### 4. Manual Testing Examples
```bash
# Login as admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Get all songs
curl -X GET http://localhost:8080/api/songs \
  -H "Authorization: Bearer YOUR_TOKEN"

# Search songs
curl -X GET "http://localhost:8080/api/songs/search?q=Bohemian" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Create playlist
curl -X POST http://localhost:8080/api/playlists \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"My Playlist","description":"Test playlist"}'

# Play a song
curl -X POST http://localhost:8080/api/playback/play/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

##  Sample Songs

The application comes pre-loaded with 10 classic songs:
1. Bohemian Rhapsody - Queen
2. Hotel California - Eagles
3. Billie Jean - Michael Jackson
4. Imagine - John Lennon
5. What's Going On - Marvin Gaye
6. Stairway to Heaven - Led Zeppelin
7. Like a Rolling Stone - Bob Dylan
8. Smells Like Teen Spirit - Nirvana
9. What'd I Say - Ray Charles
10. Yesterday - The Beatles

##  Development Notes

- **Auto-initialization**: Sample data created on startup
- **JWT Expiration**: 24 hours (configurable)
- **Timestamp Management**: Automatic creation/update timestamps
- **Database Support**: Both PostgreSQL and H2
- **CORS**: Configured for frontend integration
- **Logging**: Debug level enabled for development

##  Project Structure

```
src/main/java/com/arpit/MusicApp/
├── controller/          # REST Controllers
├── service/            # Business Logic
├── repository/         # Data Access Layer
├── entity/            # JPA Entities
├── dto/               # Data Transfer Objects
├── config/            # Configuration Classes
├── security/          # Security Components
└── util/              # Utility Classes
```


