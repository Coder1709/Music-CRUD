# File Upload Feature - Implementation Summary

## What Was Added

### 1. **Backend Components**

#### FileStorageService (`src/main/java/com/arpit/MusicApp/service/FileStorageService.java`)
- Handles file storage for audio files and cover images
- Creates unique filenames using UUID to avoid conflicts
- Validates file names for security
- Stores files in `uploads/songs/` and `uploads/covers/` directories

#### FileStorageException (`src/main/java/com/arpit/MusicApp/exception/FileStorageException.java`)
- Custom exception for file storage errors

#### New Upload Endpoint in SongController
- `POST /api/songs/admin/upload` - Multipart file upload endpoint
- Accepts audio files and optional cover images
- Requires ADMIN role
- Parameters:
  - `title` (String) - Song title
  - `artist` (String) - Artist name
  - `album` (String) - Album name
  - `genre` (Genre enum) - Music genre
  - `duration` (Integer) - Duration in seconds
  - `audioFile` (MultipartFile) - Audio file (required)
  - `coverImage` (MultipartFile) - Cover image (optional)

#### CORS Configuration (`src/main/java/com/arpit/MusicApp/config/CorsConfig.java`)
- Properly configured CORS to allow file uploads from different origins
- Supports multipart/form-data requests
- Allows all origins for development

### 2. **Frontend Components**

#### Updated HTML Interface (`src/main/resources/static/index.html`)
- Added new "Upload New Song with Files" card in Admin Operations section
- File input fields for audio and cover image
- JavaScript `uploadSong()` function that uses FormData for multipart upload

### 3. **Configuration**

#### Application Properties (`src/main/resources/application-h2.properties`)
```properties
# File Upload Configuration
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
file.upload-dir=uploads/songs
file.cover-dir=uploads/covers
```

## How to Use the Upload Feature

### Step 1: Start the Application
```bash
cd /Users/arpitpathak/Downloads/MusicApp
./mvnw spring-boot:run
```

### Step 2: Access the Interface
Open your browser and navigate to:
```
http://localhost:8080/index.html
```

### Step 3: Login as Admin
1. Use the default admin credentials:
   - Username: `admin`
   - Password: `admin123`
2. Click "Login"

### Step 4: Upload a Song
1. Navigate to "👑 Admin Operations" section
2. Find the "🎵 Upload New Song with Files" card
3. Fill in the song details:
   - Title
   - Artist
   - Album
   - Genre (select from dropdown)
   - Duration (in seconds)
4. Select an audio file (MP3, WAV, OGG, FLAC)
5. Optionally select a cover image (JPG, PNG, GIF)
6. Click "📤 Upload Song"

### Step 5: Verify Upload
- Check the response box for success message
- Navigate to "🎵 Song Management" > "Get All Songs" to see your uploaded song
- Files will be stored in:
  - Audio: `uploads/songs/[uuid].[extension]`
  - Cover: `uploads/covers/[uuid].[extension]`

## Error That Was Fixed

### CORS Error
**Problem:**
```
Access to fetch at 'http://localhost:8080/api/songs/admin/upload' 
from origin 'http://localhost:63342' has been blocked by CORS policy
```

**Solution:**
- Created `CorsConfig.java` with proper CORS settings
- Updated `SecurityConfig.java` to enable CORS
- Configured to allow:
  - All origins (development mode)
  - All HTTP methods (GET, POST, PUT, DELETE, OPTIONS)
  - All headers (including multipart/form-data)
  - Credentials (for JWT authentication)

## Testing the Feature

### Using the Web Interface
1. Login as admin
2. Go to Admin Operations
3. Upload a test audio file

### Using cURL
```bash
curl -X POST http://localhost:8080/api/songs/admin/upload \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "title=Test Song" \
  -F "artist=Test Artist" \
  -F "album=Test Album" \
  -F "genre=ROCK" \
  -F "duration=180" \
  -F "audioFile=@/path/to/your/audio.mp3" \
  -F "coverImage=@/path/to/your/cover.jpg"
```

### Using Postman
1. Create a POST request to `http://localhost:8080/api/songs/admin/upload`
2. Set Authorization header: `Bearer YOUR_JWT_TOKEN`
3. Set Body type to "form-data"
4. Add fields:
   - `title` (text)
   - `artist` (text)
   - `album` (text)
   - `genre` (text)
   - `duration` (text)
   - `audioFile` (file)
   - `coverImage` (file)
5. Send the request

## File Size Limits
- Maximum file size: **50MB**
- Maximum request size: **50MB**

To change these limits, update `application-h2.properties`:
```properties
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
```

## Security Considerations

1. **Authentication Required**: Only users with ADMIN role can upload files
2. **File Validation**: File names are validated to prevent path traversal attacks
3. **Unique Filenames**: UUID-based naming prevents file overwrites
4. **File Type Restrictions**: HTML interface limits file types via `accept` attribute

## Troubleshooting

### Issue: "Could not create directory"
**Solution**: Ensure the application has write permissions in the project directory

### Issue: "CORS error"
**Solution**: Make sure the application is running and CorsConfig is properly configured

### Issue: "413 Payload Too Large"
**Solution**: Increase `max-file-size` and `max-request-size` in application properties

### Issue: "403 Forbidden"
**Solution**: Ensure you're logged in as admin and the JWT token is valid

## Next Steps

You can enhance this feature by:
1. Adding file type validation on the backend
2. Implementing file compression
3. Adding audio metadata extraction
4. Creating thumbnails for cover images
5. Implementing cloud storage (AWS S3, Azure Blob, etc.)
6. Adding progress bars for uploads
7. Implementing file size validation before upload
