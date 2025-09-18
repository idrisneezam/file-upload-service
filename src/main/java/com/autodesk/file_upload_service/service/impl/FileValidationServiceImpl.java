package com.autodesk.file_upload_service.service.impl;

import com.autodesk.file_upload_service.config.FileValidationProperties;
import com.autodesk.file_upload_service.domain.FileMetadata;
import com.autodesk.file_upload_service.exception.FileValidationException;
import com.autodesk.file_upload_service.service.FileValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileValidationServiceImpl implements FileValidationService {
    
    private final FileValidationProperties validationProperties;
    
    @Override
    public void validateFile(FileMetadata fileMetadata) {
        if (fileMetadata == null) {
            throw new FileValidationException("File metadata cannot be null");
        }
        
        log.info("Starting file validation for: {}", fileMetadata.getOriginalFilename());
        
        validateFilename(fileMetadata.getOriginalFilename());
        validateFileSize(fileMetadata.getFileSize());
        validateContentType(fileMetadata.getContentType());
        
        log.info("File validation completed successfully for: {}", fileMetadata.getOriginalFilename());
    }
    
    @Override
    public boolean isAllowedFile(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            log.warn("Filename is null or empty");
            return false;
        }
        
        String lowerCaseFilename = filename.toLowerCase().trim();
        
        boolean isAllowed = validationProperties.getAllowedExtensions().stream()
            .anyMatch(lowerCaseFilename::endsWith);
            
        log.debug("File type validation for '{}': {}", filename, isAllowed ? "ALLOWED" : "REJECTED");
        return isAllowed;
    }
    
    private void validateFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new FileValidationException("Filename cannot be null or empty");
        }
        
        if (!isAllowedFile(filename)) {
            throw new FileValidationException(
                String.format("File type not allowed. Only %s files are permitted", validationProperties.getAllowedExtensions())
            );
        }
        
        // Check for sus chars
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new FileValidationException("Filename contains invalid characters");
        }
    }
    
    private void validateFileSize(long fileSize) {
        if (fileSize <= 0) {
            throw new FileValidationException("File size must be greater than zero");
        }
        
        if (fileSize > validationProperties.getMaxSize()) {
            throw new FileValidationException(
                String.format("File size exceeds maximum allowed size of %d bytes", validationProperties.getMaxSize())
            );
        }
    }
    
    private void validateContentType(String contentType) {
        if (contentType == null || contentType.trim().isEmpty()) {
            log.warn("Content type is null or empty, proceeding with filename-based validation");
            return;
        }
        
        boolean isValidContentType = validationProperties.getAllowedContentTypes().stream()
            .anyMatch(allowedType -> contentType.toLowerCase().contains(allowedType));
            
        if (!isValidContentType) {
            log.warn("Content type '{}' not in allowed list, but proceeding with filename validation", contentType);
        }
    }
}
