package com.arpit.MusicApp.controller;

import com.arpit.MusicApp.dto.JwtResponseDto;
import com.arpit.MusicApp.dto.LoginDto;
import com.arpit.MusicApp.dto.UserProfileDto;
import com.arpit.MusicApp.dto.UserRegistrationDto;
import com.arpit.MusicApp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * Register a new user
     * Validates input and creates user account
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        String message = authService.registerUser(registrationDto);
        return ResponseEntity.ok(message);
    }
    
    /**
     * Authenticate user and return JWT token
     * Exceptions are handled by GlobalExceptionHandler
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> authenticateUser(@RequestBody LoginDto loginDto) {
        JwtResponseDto jwtResponse = authService.authenticateUser(loginDto);
        return ResponseEntity.ok(jwtResponse);
    }
    
    /**
     * Get authenticated user's profile
     * User is extracted from security context
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfileDto profile = authService.getUserProfile(username);
        return ResponseEntity.ok(profile);
    }
}
