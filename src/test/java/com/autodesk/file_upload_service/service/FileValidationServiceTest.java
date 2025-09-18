package com.autodesk.file_upload_service.service;

import com.autodesk.file_upload_service.config.FileValidationProperties;
import com.autodesk.file_upload_service.domain.FileMetadata;
import com.autodesk.file_upload_service.exception.FileValidationException;
import com.autodesk.file_upload_service.service.impl.FileValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileValidationServiceTest {

    private FileValidationServiceImpl fileValidationService;

    @BeforeEach
    void setUp() {
        FileValidationProperties properties = new FileValidationProperties();
        fileValidationService = new FileValidationServiceImpl(properties);
    }

    @Test
    void testValidateValidTextFile() {
        // Given
        FileMetadata fileMetadata = FileMetadata.builder()
                .filename("test.txt")
                .originalFilename("test.txt")
                .fileSize(1024L)
                .contentType("text/plain")
                .uploadTimestamp(LocalDateTime.now())
                .build();

        // When & Then
        assertDoesNotThrow(() -> fileValidationService.validateFile(fileMetadata));
    }

    @Test
    void testValidateValidCsvFile() {
        // Given
        FileMetadata fileMetadata = FileMetadata.builder()
                .filename("test.csv")
                .originalFilename("test.csv")
                .fileSize(2048L)
                .contentType("text/csv")
                .uploadTimestamp(LocalDateTime.now())
                .build();

        // When & Then
        assertDoesNotThrow(() -> fileValidationService.validateFile(fileMetadata));
    }

    @Test
    void testValidateFileTooLarge() {
        // Given
        FileMetadata fileMetadata = FileMetadata.builder()
                .filename("large.txt")
                .originalFilename("large.txt")
                .fileSize(20971520L) // 20MB
                .contentType("text/plain")
                .uploadTimestamp(LocalDateTime.now())
                .build();

        // When & Then
        assertThrows(FileValidationException.class, () -> {
            fileValidationService.validateFile(fileMetadata);
        });
    }

    @Test
    void testValidateInvalidFileType() {
        // Given
        FileMetadata fileMetadata = FileMetadata.builder()
                .filename("test.pdf")
                .originalFilename("test.pdf")
                .fileSize(1024L)
                .contentType("application/pdf")
                .uploadTimestamp(LocalDateTime.now())
                .build();

        // When & Then
        assertThrows(FileValidationException.class, () -> {
            fileValidationService.validateFile(fileMetadata);
        });
    }

    @Test
    void testIsAllowedFile() {
        // When & Then
        assertTrue(fileValidationService.isAllowedFile("test.txt"));
        assertTrue(fileValidationService.isAllowedFile("test.csv"));
        assertTrue(fileValidationService.isAllowedFile("TEST.TXT"));
        assertFalse(fileValidationService.isAllowedFile("test.pdf"));
        assertFalse(fileValidationService.isAllowedFile(""));
        assertFalse(fileValidationService.isAllowedFile(null));
    }
}
