package com.autodesk.file_upload_service.controller;

import com.autodesk.file_upload_service.domain.ProcessedFile;
import com.autodesk.file_upload_service.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {
    
    private final FileUploadService fileUploadService;
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("Received file upload request for: {}", file.getOriginalFilename());
        
        ProcessedFile processedFile = fileUploadService.processUploadedFile(file);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "File uploaded and processed successfully");
        response.put("filename", processedFile.getFilename());
        response.put("lineCount", processedFile.getLineCount());
        response.put("wordCount", processedFile.getWordCount());
        response.put("processedAt", processedFile.getProcessedTimestamp());
        
        log.info("File upload completed successfully for: {}", file.getOriginalFilename());
        return ResponseEntity.ok(response);
    }
}
