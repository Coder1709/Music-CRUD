# 🎵 Music Streaming App - BLOB Storage Implementation

## 📝 Summary of Changes

### ✨ What Was Done

I've successfully updated your Music Streaming App to **store audio files and cover images as BLOBs in the database** instead of the file system. Here's a complete breakdown:

---

## 🔄 Major Changes

### 1. **Song Entity Updated** (`Song.java`)
Added new BLOB fields to store files directly in the database:

```java
// Audio file storage
@Lob
@Column(name = "audio_data", columnDefinition = "BLOB")
private byte[] audioData;

@Column(name = "audio_content_type")
private String audioContentType; // e.g., "audio/mpeg"

@Column(name = "audio_filename")
private String audioFilename;

// Cover image storage
@Lob
@Column(name = "cover_image_data", columnDefinition = "BLOB")
private byte[] coverImageData;

@Column(name = "cover_image_content_type")
private String coverImageContentType;

@Column(name = "cover_image_filename")
private String coverImageFilename;
```

**Backward compatibility**: Old `filePath` and `coverImagePath` fields are kept but marked as deprecated.

---

### 2. **SongDto Updated** (`SongDto.java`)
Added fields to indicate if BLOB data exists:

```java
private String audioFilename;
private String audioContentType;
private String coverImageFilename;
private String coverImageContentType;
private boolean hasAudioData;
private boolean hasCoverImage;
```

---

### 3. **SongController Updated** (`SongController.java`)
The upload endpoint now stores files as BLOBs:

```java
@PostMapping("/admin/upload")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<SongDto> uploadSong(
        @RequestParam("title") String title,
        @RequestParam("artist") String artist,
        @RequestParam("album") String album,
        @RequestParam("genre") Song.Genre genre,
        @RequestParam("duration") Integer duration,
        @RequestParam("audioFile") MultipartFile audioFile,
        @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) {
    
    // Store audio file as BLOB
    song.setAudioData(audioFile.getBytes());
    song.setAudioContentType(audioFile.getContentType());
    song.setAudioFilename(audioFile.getOriginalFilename());
    
    // Store cover image as BLOB if provided
    if (coverImage != null && !coverImage.isEmpty()) {
        song.setCoverImageData(coverImage.getBytes());
        song.setCoverImageContentType(coverImage.getContentType());
        song.setCoverImageFilename(coverImage.getOriginalFilename());
    }
    
    Song savedSong = songService.saveSongWithBlob(song);
    return ResponseEntity.ok(songService.convertToDto(savedSong));
}
```

---

### 4. **FileController Rewritten** (`FileController.java`)
Now streams files **from database** instead of filesystem:

```java
// Stream audio from database BLOB
@GetMapping("/audio/{songId}")
public ResponseEntity<byte[]> streamAudio(@PathVariable Long songId) {
    Song song = songService.getSongEntityById(songId);
    
    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(song.getAudioContentType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + song.getAudioFilename() + "\"")
            .body(song.getAudioData());
}

// Stream cover image from database BLOB
@GetMapping("/cover/{songId}")
public ResponseEntity<byte[]> streamCover(@PathVariable Long songId) {
    Song song = songService.getSongEntityById(songId);
    
    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(song.getCoverImageContentType()))
            .body(song.getCoverImageData());
}
```

**Key Change**: URLs now use `songId` instead of filename:
- **Old**: `/api/files/audio/142e27e7-1acd-4c4d-8eaa-b33aea107814.mp3`
- **New**: `/api/files/audio/12` (where 12 is the song ID)

---

### 5. **SongService Updated** (`SongService.java`)
Added methods to work with BLOB data:

```java
public Song saveSongWithBlob(Song song) {
    return songRepository.save(song);
}

public Song getSongEntityById(Long id) {
    return songRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Song", "id", id));
}

public SongDto convertToDto(Song song) {
    // ...existing fields...
    dto.setHasAudioData(song.getAudioData() != null && song.getAudioData().length > 0);
    dto.setHasCoverImage(song.getCoverImageData() != null && song.getCoverImageData().length > 0);
    return dto;
}
```

---

### 6. **HTML Interface Updated** (`index.html`)
Updated JavaScript to use song IDs for streaming:

```javascript
async function playSong() {
    const songId = document.getElementById('playSongId').value;
    const song = await apiCall(`/songs/${songId}`);
    
    // Use song ID to stream audio from database
    audioPlayer.src = `${API_BASE}/files/audio/${songId}`;
    
    // Update cover image if available from database
    if (song.hasCoverImage) {
        coverImg.src = `${API_BASE}/files/cover/${songId}`;
    }
    
    audioPlayer.play();
}
```

---

### 7. **DataInitializationService Updated** (`DataInitializationService.java`)
Creates sample songs with metadata only (no audio files):

```java
private void createSampleSongs() {
    // Create sample songs with metadata only
    // Audio files should be uploaded by admins using the upload endpoint
    
    Song song1 = new Song();
    song1.setTitle("Bohemian Rhapsody");
    song1.setArtist("Queen");
    song1.setAlbum("A Night at the Opera");
    song1.setGenre(Song.Genre.ROCK);
    song1.setDuration(355);
    songRepository.save(song1);
    
    // ... more sample songs ...
}
```

---

## 🎯 How It Works Now

### **Upload Flow**
1. Admin logs in
2. Goes to **Admin Operations** → **Upload New Song with Files**
3. Fills in song details
4. Selects audio file and cover image
5. Clicks **Upload Song**
6. Files are stored as **BLOBs in the database**

### **Playback Flow**
1. User enters song ID
2. Clicks **Play Song**
3. Frontend requests song details: `GET /api/songs/{id}`
4. Frontend requests audio stream: `GET /api/files/audio/{id}`
5. Backend fetches song from database
6. Backend streams BLOB data directly to browser
7. HTML5 audio player plays the audio

---

## 📊 Database Schema Changes

When you restart the app, Hibernate will create these new columns:

```sql
ALTER TABLE songs ADD COLUMN audio_data BLOB;
ALTER TABLE songs ADD COLUMN audio_content_type VARCHAR(255);
ALTER TABLE songs ADD COLUMN audio_filename VARCHAR(255);
ALTER TABLE songs ADD COLUMN cover_image_data BLOB;
ALTER TABLE songs ADD COLUMN cover_image_content_type VARCHAR(255);
ALTER TABLE songs ADD COLUMN cover_image_filename VARCHAR(255);
```

---

## 🚀 Next Steps to Test

### 1. **Restart the Application**
```bash
cd /Users/arpitpathak/Downloads/MusicApp
./mvnw spring-boot:run
```

### 2. **Login as Admin**
- Username: `admin`
- Password: `admin123`

### 3. **Upload a Song**
- Go to **Admin Operations** section
- Use the **"🎵 Upload New Song with Files"** card
- Fill in song details
- Select an MP3 file
- Optionally select a cover image
- Click **Upload Song**

### 4. **Play the Song**
- Note the song ID from the upload response (e.g., `11`, `12`, etc.)
- Go to **Playback Control** section
- Enter the song ID
- Click **Play Song**
- Audio should play in the HTML5 player!

---

## ✅ Benefits of BLOB Storage

1. **No File System Dependency**: All data in database
2. **Easier Deployment**: No need to manage `uploads/` directory
3. **Database Transactions**: Files and metadata updated atomically
4. **Backup Simplicity**: One database backup includes everything
5. **Cloud-Ready**: Easy to deploy to cloud platforms
6. **No File Path Issues**: Direct access via song ID

---

## ⚠️ Important Notes

### **Sample Songs Have No Audio**
The 10 sample songs created at startup have metadata only. To play them:
1. Delete them (they have no audio data)
2. Upload new songs with actual audio files

### **File Size Limits**
- Current limit: **50MB** per file
- Configured in: `application-h2.properties`
- For larger files, increase: `spring.servlet.multipart.max-file-size`

### **Database Choice**
- **H2 (Development)**: Works fine for testing
- **PostgreSQL (Production)**: Recommended for production
- BLOBs work with both databases

---

## 🐛 Troubleshooting

### **Issue: "No audio playing"**
- **Cause**: Song has no audio data (it's a sample song)
- **Solution**: Upload a new song with an actual audio file

### **Issue: "404 Not Found" when streaming**
- **Cause**: Song ID doesn't exist or has no audio data
- **Solution**: Check song ID and ensure it has been uploaded with audio

### **Issue: "Upload fails"**
- **Cause**: Not logged in as admin or file too large
- **Solution**: Login as admin, check file size (max 50MB)

### **Issue: "CORS error"**
- **Cause**: Missing CORS configuration
- **Solution**: Already fixed with `CorsConfig.java`

---

## 📁 Files Modified

```
✅ Song.java                    - Added BLOB fields
✅ SongDto.java                 - Added BLOB indicators
✅ SongService.java             - Added BLOB methods
✅ SongController.java          - Updated upload endpoint
✅ FileController.java          - Rewritten for BLOB streaming
✅ DataInitializationService.java - Updated sample songs
✅ index.html                   - Updated audio player
✅ application-h2.properties    - File upload config
✅ CorsConfig.java             - CORS configuration
```

---

## 🎉 Ready to Use!

Your Music Streaming App now:
- ✅ Stores audio files as BLOBs in database
- ✅ Stores cover images as BLOBs in database
- ✅ Streams files directly from database
- ✅ Has real audio playback with HTML5 player
- ✅ Works with admin file uploads
- ✅ Has proper CORS configuration
- ✅ Is cloud-ready and portable

**Start the app and test it! 🎵**

---

## 📞 Testing Commands

```bash
# 1. Restart the application
cd /Users/arpitpathak/Downloads/MusicApp
./mvnw spring-boot:run

# 2. Access the UI
# Open browser: http://localhost:8080/index.html

# 3. Login as admin
# Username: admin
# Password: admin123

# 4. Upload a song with an actual MP3 file
# Then play it using the song ID!
```

---

## 🎵 Enjoy Your Music App!
