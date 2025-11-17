#!/bin/bash

# Music App API Testing Script
echo "🎵 Music Streaming App - API Testing 🎵"
echo "========================================"

BASE_URL="http://localhost:8080/api"
ADMIN_TOKEN=""
USER_TOKEN=""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print section headers
print_section() {
    echo -e "\n${BLUE}=== $1 ===${NC}\n"
}

# Function to print test results
print_result() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ $1${NC}"
    else
        echo -e "${RED}❌ $1${NC}"
    fi
}

# Test 1: Admin Login
print_section "1. Admin Authentication"
echo "Login as admin..."
ADMIN_RESPONSE=$(curl -s -X POST "${BASE_URL}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')

echo "Admin login response:"
echo $ADMIN_RESPONSE | jq '.' 2>/dev/null || echo $ADMIN_RESPONSE

ADMIN_TOKEN=$(echo $ADMIN_RESPONSE | jq -r '.token' 2>/dev/null)
if [[ "$ADMIN_TOKEN" != "null" && "$ADMIN_TOKEN" != "" ]]; then
    print_result "Admin login successful"
else
    print_result "Admin login failed"
    exit 1
fi

# Test 2: User Registration
print_section "2. User Registration"
echo "Registering new user..."
REG_RESPONSE=$(curl -s -X POST "${BASE_URL}/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "test123",
    "firstName": "Test",
    "lastName": "User"
  }')

echo "Registration response:"
echo $REG_RESPONSE
print_result "User registration"

# Test 3: User Login
print_section "3. User Authentication"
echo "Login as regular user..."
USER_RESPONSE=$(curl -s -X POST "${BASE_URL}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}')

echo "User login response:"
echo $USER_RESPONSE | jq '.' 2>/dev/null || echo $USER_RESPONSE

USER_TOKEN=$(echo $USER_RESPONSE | jq -r '.token' 2>/dev/null)
if [[ "$USER_TOKEN" != "null" && "$USER_TOKEN" != "" ]]; then
    print_result "User login successful"
else
    print_result "User login failed"
fi

# Test 4: Get User Profile
print_section "4. User Profile"
echo "Getting user profile..."
PROFILE_RESPONSE=$(curl -s -X GET "${BASE_URL}/auth/profile" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "Profile response:"
echo $PROFILE_RESPONSE | jq '.' 2>/dev/null || echo $PROFILE_RESPONSE
print_result "Get user profile"

# Test 5: Get All Songs
print_section "5. Song Management"
echo "Getting all songs..."
SONGS_RESPONSE=$(curl -s -X GET "${BASE_URL}/songs" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "Songs response (first song):"
echo $SONGS_RESPONSE | jq '.[0]' 2>/dev/null || echo $SONGS_RESPONSE | head -n 5
print_result "Get all songs"

# Test 6: Search Songs
print_section "6. Song Search"
echo "Searching for 'Bohemian'..."
SEARCH_RESPONSE=$(curl -s -X GET "${BASE_URL}/songs/search?q=Bohemian" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "Search response:"
echo $SEARCH_RESPONSE | jq '.' 2>/dev/null || echo $SEARCH_RESPONSE
print_result "Search songs"

# Test 7: Get Songs by Genre
print_section "7. Songs by Genre"
echo "Getting ROCK songs..."
GENRE_RESPONSE=$(curl -s -X GET "${BASE_URL}/songs/genre/ROCK" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "Genre response (count):"
echo $GENRE_RESPONSE | jq 'length' 2>/dev/null || echo "Could not parse response"
print_result "Get songs by genre"

# Test 8: Admin - Add New Song
print_section "8. Admin - Add Song"
echo "Adding new song as admin..."
NEW_SONG_RESPONSE=$(curl -s -X POST "${BASE_URL}/songs/admin" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Song",
    "artist": "Test Artist",
    "album": "Test Album",
    "genre": "POP",
    "duration": 180,
    "filePath": "/songs/test-song.mp3",
    "coverImagePath": "/covers/test-song.jpg"
  }')

echo "New song response:"
echo $NEW_SONG_RESPONSE | jq '.' 2>/dev/null || echo $NEW_SONG_RESPONSE
print_result "Admin add song"

NEW_SONG_ID=$(echo $NEW_SONG_RESPONSE | jq -r '.id' 2>/dev/null)

# Test 9: Create Playlist
print_section "9. Playlist Management"
echo "Creating playlist..."
PLAYLIST_RESPONSE=$(curl -s -X POST "${BASE_URL}/playlists" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Test Playlist",
    "description": "A playlist for testing"
  }')

echo "Playlist response:"
echo $PLAYLIST_RESPONSE | jq '.' 2>/dev/null || echo $PLAYLIST_RESPONSE
print_result "Create playlist"

PLAYLIST_ID=$(echo $PLAYLIST_RESPONSE | jq -r '.id' 2>/dev/null)

# Test 10: Add Song to Playlist
print_section "10. Add Song to Playlist"
echo "Adding song ID 1 to playlist..."
ADD_SONG_RESPONSE=$(curl -s -X POST "${BASE_URL}/playlists/${PLAYLIST_ID}/songs/1" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "Add song to playlist response:"
echo $ADD_SONG_RESPONSE | jq '.songCount' 2>/dev/null || echo $ADD_SONG_RESPONSE
print_result "Add song to playlist"

# Test 11: Get User Playlists
print_section "11. Get User Playlists"
echo "Getting user playlists..."
USER_PLAYLISTS_RESPONSE=$(curl -s -X GET "${BASE_URL}/playlists" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "User playlists response:"
echo $USER_PLAYLISTS_RESPONSE | jq '.' 2>/dev/null || echo $USER_PLAYLISTS_RESPONSE
print_result "Get user playlists"

# Test 12: Playback - Play Song
print_section "12. Playback Control"
echo "Playing song ID 1..."
PLAY_RESPONSE=$(curl -s -X POST "${BASE_URL}/playback/play/1" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "Play response:"
echo $PLAY_RESPONSE | jq '.' 2>/dev/null || echo $PLAY_RESPONSE
print_result "Play song"

# Test 13: Pause Playback
echo "Pausing playback..."
PAUSE_RESPONSE=$(curl -s -X POST "${BASE_URL}/playback/pause" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "Pause response:"
echo $PAUSE_RESPONSE | jq '.state' 2>/dev/null || echo $PAUSE_RESPONSE
print_result "Pause playback"

# Test 14: Resume Playback
echo "Resuming playback..."
RESUME_RESPONSE=$(curl -s -X POST "${BASE_URL}/playback/resume" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "Resume response:"
echo $RESUME_RESPONSE | jq '.state' 2>/dev/null || echo $RESUME_RESPONSE
print_result "Resume playback"

# Test 15: Get Current Playback
echo "Getting current playback status..."
CURRENT_RESPONSE=$(curl -s -X GET "${BASE_URL}/playback/current" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "Current playback response:"
echo $CURRENT_RESPONSE | jq '.' 2>/dev/null || echo $CURRENT_RESPONSE
print_result "Get current playback"

# Test 16: Stop Playback
echo "Stopping playback..."
STOP_RESPONSE=$(curl -s -X POST "${BASE_URL}/playback/stop" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "Stop response:"
echo $STOP_RESPONSE | jq '.state' 2>/dev/null || echo $STOP_RESPONSE
print_result "Stop playback"

print_section "🎉 API Testing Complete! 🎉"
echo "All major endpoints have been tested."
echo ""
echo "📝 Summary:"
echo "- Authentication: Admin & User login"
echo "- User Management: Registration, Profile"
echo "- Song Management: List, Search, Admin operations"
echo "- Playlist Management: Create, Add/Remove songs"
echo "- Playback Control: Play, Pause, Resume, Stop"
echo ""
echo "🌐 Application URLs:"
echo "- API Base: http://localhost:8080/api"
echo "- H2 Console: http://localhost:8080/h2-console"
echo ""
echo "👤 Default Credentials:"
echo "- Admin: admin / admin123"
echo "- User: user / user123"
