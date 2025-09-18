package com.autodesk.file_upload_service.service;

import com.autodesk.file_upload_service.domain.ProcessedFile;
import com.autodesk.file_upload_service.service.impl.FileProcessingServiceImpl;
import com.autodesk.file_upload_service.config.FileProcessingProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleFileProcessingServiceTest {

    private FileProcessingServiceImpl fileProcessingService;

    @BeforeEach
    void setUp() {
        FileProcessingProperties properties = 
            new com.autodesk.file_upload_service.config.FileProcessingProperties();
        fileProcessingService = new FileProcessingServiceImpl(properties);
    }

    @Test
    void testProcessTextFile() {
        // Given
        String content = "Hello World!\nThis is a test file.\nIt has multiple lines.";
        String filename = "test.txt";

        // When
        ProcessedFile result = fileProcessingService.processFile(content, filename);

        // Then
        assertNotNull(result);
        assertEquals(filename, result.getFilename());
        assertEquals(3, result.getLineCount());
        assertEquals(9, result.getWordCount());
        assertEquals(content, result.getFileContent());
        assertNotNull(result.getProcessedTimestamp());
    }

    @Test
    void testProcessCsvFile() {
        // Given
        String content = "Name,Age\nJohn,25\nJane,30";
        String filename = "test.csv";

        // When
        ProcessedFile result = fileProcessingService.processFile(content, filename);

        // Then
        assertNotNull(result);
        assertEquals(filename, result.getFilename());
        assertEquals(3, result.getLineCount());
        assertEquals(6, result.getWordCount());
        assertEquals(content, result.getFileContent());
        assertNotNull(result.getProcessedTimestamp());
    }

    @Test
    void testCountLines() {
        // Given
        String content = "Line 1\nLine 2\nLine 3";

        // When
        long lineCount = fileProcessingService.countLines(content);

        // Then
        assertEquals(3, lineCount);
    }

    @Test
    void testCountWords() {
        // Given
        String content = "Hello world this is a test";

        // When
        long wordCount = fileProcessingService.countWords(content);

        // Then
        assertEquals(6, wordCount);
    }

}
