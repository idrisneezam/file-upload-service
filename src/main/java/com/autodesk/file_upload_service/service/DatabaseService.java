package com.autodesk.file_upload_service.service;

import com.autodesk.file_upload_service.domain.ProcessedFile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DatabaseService {
    
    public void saveToDatabase(ProcessedFile processedFile) {
        // TODO: implement database save logic
    }
    
    public Optional<ProcessedFile> findByFilename(String filename) {
        // TODO: implement database find logic
        return Optional.empty();
    }
    
    public void updateFile(ProcessedFile processedFile) {
        // TODO: implement database update logic
    }
}
