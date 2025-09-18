package com.autodesk.file_upload_service.service;

import com.autodesk.file_upload_service.domain.FileMetadata;

public interface FileValidationService {
    
    /**
     * Validates a file based on its metadata
     * @param fileMetadata the file metadata to validate
     * @throws com.autodesk.file_upload_service.exception.FileValidationException if validation fails
     */
    void validateFile(FileMetadata fileMetadata);
    
    /**
     * Checks if a file type is allowed
     * @param filename the filename to check
     * @return true if the file type is allowed, false otherwise
     */
    boolean isAllowedFile(String filename);
}