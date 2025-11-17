package com.arpit.MusicApp.service;

import com.arpit.MusicApp.dto.JwtResponseDto;
import com.arpit.MusicApp.dto.LoginDto;
import com.arpit.MusicApp.dto.UserProfileDto;
import com.arpit.MusicApp.dto.UserRegistrationDto;
import com.arpit.MusicApp.entity.User;
import com.arpit.MusicApp.repository.UserRepository;
import com.arpit.MusicApp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public String registerUser(UserRegistrationDto registrationDto) {
        // Check if username already exists - business validation
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new com.arpit.MusicApp.exception.BusinessValidationException(
                "Username is already taken!", "DUPLICATE_USERNAME");
        }
        
        // Check if email already exists - business validation
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new com.arpit.MusicApp.exception.BusinessValidationException(
                "Email is already in use!", "DUPLICATE_EMAIL");
        }
        
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setRole(User.Role.USER);
        
        userRepository.save(user);
        return "User registered successfully!";
    }
    
    public JwtResponseDto authenticateUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken((User) authentication.getPrincipal());
        
        User user = (User) authentication.getPrincipal();
        
        return new JwtResponseDto(jwt, user.getUsername(), user.getEmail(), user.getRole().name());
    }
    
    public UserProfileDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new com.arpit.MusicApp.exception.ResourceNotFoundException(
                    "User", "username", username));
        
        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setId(user.getId());
        profileDto.setUsername(user.getUsername());
        profileDto.setEmail(user.getEmail());
        profileDto.setFirstName(user.getFirstName());
        profileDto.setLastName(user.getLastName());
        profileDto.setRole(user.getRole().name());
        profileDto.setPlaylistCount(user.getPlaylists() != null ? user.getPlaylists().size() : 0);
        
        return profileDto;
    }
}
