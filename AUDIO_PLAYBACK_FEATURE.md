# 🎵 Music Streaming App - Audio Playback Feature

## ✨ New Features Added

### 1. **File Upload System** 
- **FileStorageService**: Handles file storage for audio files and cover images
- **FileController**: REST endpoints to stream audio and images
- Files are stored in: `uploads/songs/` and `uploads/covers/`

### 2. **Real Audio Playback**
- HTML5 audio player integrated into the UI
- Real-time audio streaming from backend
- Cover image display
- Playback controls (Play, Pause, Resume, Stop)

### 3. **CORS Configuration**
- Proper CORS setup for cross-origin file uploads
- Supports multipart/form-data requests
- Allows credentials and all necessary headers

---

## 🎯 How to Use

### **Step 1: Login as Admin**
1. Go to the **Authentication** section
2. Use credentials: `admin` / `admin123`
3. Click **Login**

### **Step 2: Upload a Song**
1. Navigate to **Admin Operations** section
2. Find **"Upload New Song with Files"** card
3. Fill in song details:
   - Title
   - Artist
   - Album
   - Genre
   - Duration (in seconds)
4. **Select Audio File** (MP3, WAV, OGG, etc.)
5. **Select Cover Image** (optional - JPG, PNG, GIF)
6. Click **"📤 Upload Song"**

### **Step 3: Play the Song**
1. Go to **Playback Control** section
2. Enter the **Song ID** (you'll see it in the upload response)
3. Click **"▶️ Play Song"**
4. The audio player will load and start playing automatically!

---

## 🔧 API Endpoints

### File Streaming Endpoints
```
GET /api/files/audio/{filename}     - Stream audio file
GET /api/files/cover/{filename}     - Stream cover image
```

### Song Upload Endpoint
```
POST /api/songs/admin/upload        - Upload song with files
Parameters:
  - title: String
  - artist: String
  - album: String
  - genre: Genre enum
  - duration: Integer (seconds)
  - audioFile: MultipartFile (required)
  - coverImage: MultipartFile (optional)
```

---

## 📋 Configuration

### File Upload Limits (in application-h2.properties)
```properties
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
file.upload-dir=uploads/songs
file.cover-dir=uploads/covers
```

---

## 🎨 UI Components

### Now Playing Card
- Displays current song title and artist
- Shows album cover (if available)
- HTML5 audio player with standard controls
- Visual feedback with gradient background

### Playback Controls
- **Play Song**: Fetches song data and starts playback
- **Pause**: Pauses the audio
- **Resume**: Resumes paused audio
- **Stop**: Stops playback and clears player
- **Get Current Status**: Shows backend playback state

---

## 🐛 Troubleshooting

### No Audio Playing?
1. **Check if file exists**: Verify the audio file was uploaded successfully
2. **Check browser console**: Look for any error messages
3. **Check file path**: Ensure the file path in database matches actual file location
4. **Check CORS**: Make sure CORS is properly configured

### CORS Errors?
- The app includes comprehensive CORS configuration
- Allowed origins: `*` (all origins for development)
- Allowed methods: GET, POST, PUT, DELETE, OPTIONS, PATCH
- Allowed headers: All headers including Authorization

### File Upload Fails?
1. **Check file size**: Max 50MB
2. **Check file format**: Audio files (MP3, WAV, OGG, FLAC)
3. **Check admin auth**: Must be logged in as admin
4. **Check uploads directory**: Ensure `uploads/songs` and `uploads/covers` exist

---

## 📁 File Structure

```
MusicApp/
├── src/main/java/com/arpit/MusicApp/
│   ├── controller/
│   │   ├── FileController.java      ✨ NEW - File streaming
│   │   └── SongController.java      🔄 UPDATED - Upload endpoint
│   ├── service/
│   │   └── FileStorageService.java  ✨ NEW - File storage logic
│   ├── exception/
│   │   └── FileStorageException.java ✨ NEW - Custom exception
│   └── config/
│       └── CorsConfig.java          ✨ NEW - CORS configuration
├── src/main/resources/
│   ├── static/
│   │   └── index.html               🔄 UPDATED - Audio player UI
│   └── application-h2.properties    🔄 UPDATED - File upload config
└── uploads/                          ✨ NEW - File storage
    ├── songs/                        ✨ NEW - Audio files
    └── covers/                       ✨ NEW - Cover images
```

---

## 🚀 Testing Guide

### Test 1: Upload a Song
```bash
# Login as admin first, get the JWT token
# Then use the UI to upload a song with an audio file
```

### Test 2: Play the Song
```bash
# 1. Note the song ID from upload response
# 2. Enter song ID in "Play Song" input
# 3. Click "Play Song" button
# 4. Audio should start playing automatically
```

### Test 3: Playback Controls
```bash
# While song is playing:
# - Click "Pause" to pause
# - Click "Resume" to continue
# - Click "Stop" to stop and reset
```

---

## 📊 Technical Details

### Audio Streaming
- Uses Spring's `Resource` and `UrlResource` for file streaming
- Content-Type automatically detected
- Supports range requests for seeking
- Inline content disposition for browser playback

### File Storage
- UUID-based filenames to prevent conflicts
- Original file extensions preserved
- Secure path validation (prevents directory traversal)
- Automatic directory creation

### Security
- Admin-only upload endpoint
- JWT authentication required
- CORS properly configured
- File path sanitization

---

## ✅ All Features Working

- ✅ User authentication (Login/Register)
- ✅ Song management (CRUD operations)
- ✅ **File upload with audio and cover images**
- ✅ **Real audio playback with HTML5 player**
- ✅ **File streaming endpoints**
- ✅ Playlist management
- ✅ Playback state tracking
- ✅ CORS configuration
- ✅ Admin operations

---

## 🎉 Ready to Use!

Your Music Streaming App now has full audio playback capabilities! You can:
1. Upload songs with actual audio files
2. Play them in the browser
3. Control playback (play, pause, resume, stop)
4. See cover images
5. Track playback state

**Enjoy your music! 🎵**
