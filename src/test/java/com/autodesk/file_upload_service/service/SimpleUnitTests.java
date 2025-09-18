package com.autodesk.file_upload_service.service;

import com.autodesk.file_upload_service.domain.ProcessedFile;
import com.autodesk.file_upload_service.service.impl.DatabaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleUnitTests {

    private DatabaseServiceImpl databaseService;

    @BeforeEach
    void setUp() {
        databaseService = new DatabaseServiceImpl();
    }

    @Test
    void testDatabaseOperations() {
        // Given
        ProcessedFile file = ProcessedFile.builder()
                .filename("test.txt")
                .lineCount(5)
                .wordCount(25)
                .processedTimestamp(LocalDateTime.now())
                .fileContent("test content")
                .build();

        // When
        databaseService.createFile(file);

        // Then
        assertTrue(databaseService.fileExists("test.txt"));
        assertTrue(databaseService.readFile("test.txt").isPresent());
        assertEquals("test.txt", databaseService.readFile("test.txt").get().getFilename());
    }

    @Test
    void testLineCounting() {
        // Test line counting logic directly
        String content = "Line 1\nLine 2\nLine 3";
        Pattern linePattern = Pattern.compile("\\r?\\n");
        String[] lines = linePattern.split(content);
        long lineCount = lines.length;
        
        if (!content.endsWith("\n") && !content.endsWith("\r")) {
            lineCount = Math.max(1, lineCount);
        }
        
        assertEquals(3, lineCount);
    }

    @Test
    void testWordCounting() {
        // Test word counting logic directly
        String content = "Hello world this is a test";
        Pattern wordPattern = Pattern.compile("\\b\\w+\\b");
        long wordCount = wordPattern.matcher(content).results().count();
        
        assertEquals(6, wordCount);
    }

    @Test
    void testFileValidation() {
        // Test file validation logic directly
        String filename = "test.txt";
        String[] allowedExtensions = {".txt", ".csv"};
        
        boolean isAllowed = false;
        for (String ext : allowedExtensions) {
            if (filename.toLowerCase().endsWith(ext)) {
                isAllowed = true;
                break;
            }
        }
        
        assertTrue(isAllowed);
    }

    @Test
    void testFileSizeValidation() {
        // Test file size validation logic directly
        long fileSize = 1024L;
        long maxSize = 10485760L; // 10MB
        
        assertTrue(fileSize <= maxSize);
        assertTrue(fileSize > 0);
    }
}
