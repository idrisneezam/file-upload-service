package com.autodesk.file_upload_service.service.impl;

import com.autodesk.file_upload_service.domain.FileMetadata;
import com.autodesk.file_upload_service.domain.ProcessedFile;
import com.autodesk.file_upload_service.service.DatabaseService;
import com.autodesk.file_upload_service.service.FileProcessingService;
import com.autodesk.file_upload_service.service.FileUploadService;
import com.autodesk.file_upload_service.service.FileValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    
    private final FileValidationService fileValidationService;
    private final FileProcessingService fileProcessingService;
    private final DatabaseService databaseService;
    
    @Override
    public ProcessedFile processUploadedFile(MultipartFile file) {
        log.info("Processing uploaded file: {}", file.getOriginalFilename());
        
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        
        FileMetadata fileMetadata = FileMetadata.builder()
            .filename(file.getOriginalFilename())
            .originalFilename(file.getOriginalFilename())
            .fileSize(file.getSize())
            .contentType(file.getContentType())
            .uploadTimestamp(LocalDateTime.now())
            .build();
        
        fileValidationService.validateFile(fileMetadata);
        
        String fileContent;
        try {
            fileContent = new String(file.getBytes());
            log.debug("Read {} bytes from file: {}", fileContent.length(), file.getOriginalFilename());
        } catch (Exception e) {
            log.error("Error reading file content: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read file content", e);
        }
        
        ProcessedFile processedFile = fileProcessingService.processFile(fileContent, file.getOriginalFilename());
        
        // Upsert to db (create if not exists, update if exists)
        if (databaseService.fileExists(processedFile.getFilename())) {
            log.info("File '{}' already exists, updating entry", processedFile.getFilename());
            databaseService.updateFile(processedFile);
        } else {
            log.info("File '{}' does not exist, creating new entry", processedFile.getFilename());
            databaseService.createFile(processedFile);
        }
        
        log.info("Successfully processed and saved file: {}", file.getOriginalFilename());
        return processedFile;
    }
}
