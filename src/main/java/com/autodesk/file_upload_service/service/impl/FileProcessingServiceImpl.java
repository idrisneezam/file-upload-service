package com.autodesk.file_upload_service.service.impl;

import com.autodesk.file_upload_service.config.FileProcessingProperties;
import com.autodesk.file_upload_service.domain.ProcessedFile;
import com.autodesk.file_upload_service.exception.FileProcessingException;
import com.autodesk.file_upload_service.service.FileProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileProcessingServiceImpl implements FileProcessingService {
    
    private final FileProcessingProperties processingProperties;
    
    @Override
    public ProcessedFile processFile(String fileContent, String filename) {
        log.info("Starting file processing for: {}", filename);
        
        if (fileContent == null) {
            throw new FileProcessingException("File content cannot be null");
        }
        
        if (filename == null || filename.trim().isEmpty()) {
            throw new FileProcessingException("Filename cannot be null or empty");
        }
        
        try {
            long lineCount = countLines(fileContent);
            long wordCount = countWords(fileContent);
            
            ProcessedFile processedFile = ProcessedFile.builder()
                .filename(filename)
                .lineCount(lineCount)
                .wordCount(wordCount)
                .processedTimestamp(LocalDateTime.now())
                .fileContent(fileContent)
                .build();
                
            log.info("File processing completed for '{}': {} lines, {} words", 
                filename, lineCount, wordCount);
                
            return processedFile;
            
        } catch (Exception e) {
            log.error("Error processing file '{}': {}", filename, e.getMessage(), e);
            throw new FileProcessingException("Failed to process file: " + filename, e);
        }
    }
    
    @Override
    public long countLines(String content) {
        if (content == null || content.isEmpty()) {
            log.debug("Content is null or empty, returning 0 lines");
            return 0;
        }
        
        try {
            // Handling different line ending formats using configured pattern
            Pattern linePattern = Pattern.compile(processingProperties.getLinePattern());
            String[] lines = linePattern.split(content);
            
            // If content does not end with a newline, we need to add 1 for the last line
            long lineCount = lines.length;
            if (!content.endsWith("\n") && !content.endsWith("\r")) {
                lineCount = Math.max(1, lineCount); // At least 1 line if content exists
            }
            
            log.debug("Counted {} lines in content using pattern: {}", lineCount, processingProperties.getLinePattern());
            return lineCount;
            
        } catch (Exception e) {
            log.error("Error counting lines: {}", e.getMessage(), e);
            throw new FileProcessingException("Failed to count lines", e);
        }
    }
    
    @Override
    public long countWords(String content) {
        if (content == null || content.isEmpty()) {
            log.debug("Content is null or empty, returning 0 words");
            return 0;
        }
        
        try {
            // Use configured regex pattern to find word boundaries
            Pattern wordPattern = Pattern.compile(processingProperties.getWordPattern());
            long wordCount = wordPattern.matcher(content).results().count();
            
            log.debug("Counted {} words in content using pattern: {}", wordCount, processingProperties.getWordPattern());
            return wordCount;
            
        } catch (Exception e) {
            log.error("Error counting words: {}", e.getMessage(), e);
            throw new FileProcessingException("Failed to count words", e);
        }
    }
}
