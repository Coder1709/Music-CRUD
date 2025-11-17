# 🎵 QUICK REFERENCE - Music App Audio Playback

## 🚀 START THE APP
```bash
cd /Users/arpitpathak/Downloads/MusicApp
./mvnw spring-boot:run
```

## 🌐 OPEN WEB INTERFACE
```
http://localhost:63342/MusicApp/src/main/resources/static/index.html
```

## 🔑 DEFAULT CREDENTIALS
- **Admin**: `admin` / `admin123`
- **User**: `user` / `user123`

## 📤 UPLOAD A SONG
1. Login as admin
2. Go to **Admin Operations** tab
3. Fill in song details
4. Select audio file (MP3/WAV)
5. Optionally select cover image
6. Click **"📤 Upload Song"**

## ▶️ PLAY A SONG
1. Go to **Playback Control** tab
2. Enter song ID
3. Click **"▶️ Play Song"**
4. Audio plays automatically! 🎉

## 🔧 KEY ENDPOINTS

### Public (No Auth)
- `GET /api/files/audio/{songId}` - Stream audio
- `GET /api/files/cover/{songId}` - Stream cover image
- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Register

### Protected (JWT Required)
- `POST /api/songs/admin/upload` - Upload song (Admin only)
- `GET /api/songs` - List all songs
- `POST /api/playback/play/{songId}` - Play song
- `POST /api/playlists` - Create playlist

## ✅ WHAT'S WORKING
- ✅ File upload (multipart/form-data)
- ✅ BLOB storage in database
- ✅ Audio streaming (no auth required)
- ✅ HTML5 audio player
- ✅ Cover art display
- ✅ Playback controls
- ✅ CORS configured
- ✅ JWT authentication

## 🐛 TROUBLESHOOTING

### Audio not playing?
- Check song has audio data: `"hasAudioData": true`
- Verify endpoint is public: `/api/files/**` in SecurityConfig
- Check browser console for errors
- Restart application

### 401 Unauthorized?
- **Fixed!** `/api/files/**` is now public
- Restart application if needed

### CORS errors?
- CORS is configured in `CorsConfig.java`
- Allows all origins for development
- Restart application if needed

## 📊 FILE SIZES
- Max file size: **50MB** (configurable)
- Supported audio: MP3, WAV, OGG, FLAC
- Supported images: JPG, PNG, GIF

## 💾 DATABASE
Audio files and images stored as BLOBs in:
- `songs.audio_data` (BLOB)
- `songs.cover_image_data` (BLOB)

## 📝 SAMPLE TEST

### Upload via cURL
```bash
curl -X POST http://localhost:8080/api/songs/admin/upload \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "title=Test Song" \
  -F "artist=Test Artist" \
  -F "album=Test Album" \
  -F "genre=POP" \
  -F "duration=180" \
  -F "audioFile=@song.mp3" \
  -F "coverImage=@cover.jpg"
```

### Play in Browser
```javascript
// No authentication needed for streaming!
const audio = new Audio('http://localhost:8080/api/files/audio/11');
audio.play();
```

## 🎉 SUCCESS!
Your Music Streaming App is **FULLY FUNCTIONAL** with:
- Database BLOB storage
- Real audio playback
- File uploads
- Secure authentication
- Beautiful UI

**Enjoy! 🎵**
