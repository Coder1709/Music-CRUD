package com.arpit.MusicApp.service;

import com.arpit.MusicApp.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final Path coverStorageLocation;

    public FileStorageService(@Value("${file.upload-dir:uploads/songs}") String uploadDir,
                             @Value("${file.cover-dir:uploads/covers}") String coverDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.coverStorageLocation = Paths.get(coverDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.coverStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * Store an audio file
     */
    public String storeAudioFile(MultipartFile file) {
        return storeFile(file, fileStorageLocation, "audio");
    }

    /**
     * Store a cover image file
     */
    public String storeCoverImage(MultipartFile file) {
        return storeFile(file, coverStorageLocation, "cover");
    }

    /**
     * Generic file storage method
     */
    private String storeFile(MultipartFile file, Path location, String type) {
        // Normalize file name
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new FileStorageException("File name is empty");
        }
        originalFileName = StringUtils.cleanPath(originalFileName);

        try {
            // Check if the file's name contains invalid characters
            if (originalFileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            // Generate unique filename to avoid conflicts
            String fileExtension = "";
            if (originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = location.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Return relative path
            return "/" + location.getFileName() + "/" + uniqueFileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store " + type + " file " + originalFileName + ". Please try again!", ex);
        }
    }

    /**
     * Delete a file
     */
    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            // Log the error but don't throw exception
            System.err.println("Could not delete file: " + filePath);
        }
    }
}
