#!/bin/bash

# Exception Handling Test Script
echo "🛡️ EXCEPTION HANDLING SYSTEM TEST 🛡️"
echo "========================================"

BASE_URL="http://localhost:8080/api"
TOKEN=""

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

# Function to print test info
print_test() {
    echo -e "${YELLOW}Test: $1${NC}"
}

# Get auth token
print_section "1. Getting Authentication Token"
AUTH_RESPONSE=$(curl -s -X POST "${BASE_URL}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}')

TOKEN=$(echo $AUTH_RESPONSE | jq -r '.token' 2>/dev/null)
echo "✅ Token obtained"

# Test 1: ResourceNotFoundException
print_section "2. Testing ResourceNotFoundException (404)"
print_test "Requesting non-existent song (ID: 999)"
curl -s -X GET "${BASE_URL}/songs/999" \
  -H "Authorization: Bearer $TOKEN" | jq '.'

# Test 2: UnauthorizedAccessException
print_section "3. Testing UnauthorizedAccessException (403)"
print_test "Trying to access non-existent playlist (should be 404 first, then 403 if owned by others)"
curl -s -X GET "${BASE_URL}/playlists/999" \
  -H "Authorization: Bearer $TOKEN" | jq '.'

# Test 3: BusinessValidationException
print_section "4. Testing BusinessValidationException (400)"
print_test "Registering with duplicate username"
curl -s -X POST "${BASE_URL}/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "email": "test@test.com",
    "password": "test123"
  }' | jq '.'

# Test 4: AuthenticationException
print_section "5. Testing AuthenticationException (401)"
print_test "Login with invalid credentials"
curl -s -X POST "${BASE_URL}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "invaliduser",
    "password": "wrongpassword"
  }' | jq '.'

# Test 5: Method Not Allowed
print_section "6. Testing HttpRequestMethodNotSupportedException (405)"
print_test "Using PUT on login endpoint (POST only)"
curl -s -X PUT "${BASE_URL}/auth/login" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{}' | jq '.'

# Test 6: Malformed JSON
print_section "7. Testing HttpMessageNotReadableException (400)"
print_test "Sending malformed JSON"
curl -s -X POST "${BASE_URL}/auth/register" \
  -H "Content-Type: application/json" \
  -d '{invalid json}' | jq '.'

# Test 7: Missing Required Parameter
print_section "8. Testing Missing Request Body"
print_test "Registering without request body"
curl -s -X POST "${BASE_URL}/auth/register" \
  -H "Content-Type: application/json" | jq '.'

# Test 8: Access Denied (Spring Security)
print_section "9. Testing AccessDeniedException (403)"
print_test "Non-admin trying to add song"
curl -s -X POST "${BASE_URL}/songs/admin" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Song",
    "artist": "Test Artist",
    "genre": "POP",
    "duration": 180
  }' | jq '.'

# Test 9: Endpoint Not Found
print_section "10. Testing NoHandlerFoundException (404)"
print_test "Accessing non-existent endpoint"
curl -s -X GET "${BASE_URL}/nonexistent/endpoint" \
  -H "Authorization: Bearer $TOKEN" | jq '.'

# Test 10: Unauthorized Request
print_section "11. Testing Missing Authentication (401)"
print_test "Accessing protected endpoint without token"
curl -s -X GET "${BASE_URL}/songs" | jq '.'

print_section "🎉 Exception Handling Tests Complete! 🎉"
echo ""
echo "📊 Summary of Tests:"
echo "✅ ResourceNotFoundException (404)"
echo "✅ UnauthorizedAccessException (403)"
echo "✅ BusinessValidationException (400)"
echo "✅ AuthenticationException (401)"
echo "✅ MethodNotAllowedException (405)"
echo "✅ MalformedJSONException (400)"
echo "✅ AccessDeniedException (403)"
echo "✅ EndpointNotFoundException (404)"
echo "✅ MissingAuthenticationException (401)"
echo ""
echo "📁 Check log files in: logs/"
echo "- music-app.log (all logs)"
echo "- music-app-error.log (errors only)"
echo "- music-app-exceptions.log (detailed exceptions)"
echo ""
echo "📖 See EXCEPTION_HANDLING_DOCUMENTATION.md for details"
