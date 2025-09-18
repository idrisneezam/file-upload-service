package com.autodesk.file_upload_service.service;

import com.autodesk.file_upload_service.domain.FileMetadata;
import org.springframework.stereotype.Service;

@Service
public class FileValidationService {
    
    public void validateFile(FileMetadata fileMetadata) {
        // TODO: implement file validation logic for edge cases
    }
    
    public boolean isAllowedFile(String filename) {
        // TODO: implement allowed file type validation
        return false;
    }
}
