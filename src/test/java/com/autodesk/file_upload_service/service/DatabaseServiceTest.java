package com.autodesk.file_upload_service.service;

import com.autodesk.file_upload_service.domain.ProcessedFile;
import com.autodesk.file_upload_service.service.impl.DatabaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseServiceTest {

    private DatabaseServiceImpl databaseService;

    @BeforeEach
    void setUp() {
        databaseService = new DatabaseServiceImpl();
    }

    @Test
    void testCreateFile() {
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
    void testUpdateFile() {
        // Given
        ProcessedFile originalFile = ProcessedFile.builder()
                .filename("test.txt")
                .lineCount(5)
                .wordCount(25)
                .processedTimestamp(LocalDateTime.now())
                .fileContent("original content")
                .build();

        ProcessedFile updatedFile = ProcessedFile.builder()
                .filename("test.txt")
                .lineCount(10)
                .wordCount(50)
                .processedTimestamp(LocalDateTime.now())
                .fileContent("updated content")
                .build();

        // When
        databaseService.createFile(originalFile);
        databaseService.updateFile(updatedFile);

        // Then
        assertTrue(databaseService.fileExists("test.txt"));
        ProcessedFile retrievedFile = databaseService.readFile("test.txt").get();
        assertEquals(10, retrievedFile.getLineCount());
        assertEquals(50, retrievedFile.getWordCount());
        assertEquals("updated content", retrievedFile.getFileContent());
    }

    @Test
    void testFileExists() {
        // Given
        ProcessedFile file = ProcessedFile.builder()
                .filename("test.txt")
                .lineCount(5)
                .wordCount(25)
                .processedTimestamp(LocalDateTime.now())
                .fileContent("test content")
                .build();

        // When & Then
        assertFalse(databaseService.fileExists("test.txt"));
        
        databaseService.createFile(file);
        assertTrue(databaseService.fileExists("test.txt"));
    }

    @Test
    void testReadFile() {
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
        assertTrue(databaseService.readFile("test.txt").isPresent());
        assertFalse(databaseService.readFile("nonexistent.txt").isPresent());
    }
}
