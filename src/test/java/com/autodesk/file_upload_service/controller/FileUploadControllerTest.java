package com.autodesk.file_upload_service.controller;

import com.autodesk.file_upload_service.domain.ProcessedFile;
import com.autodesk.file_upload_service.service.FileUploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileUploadController.class)
@SuppressWarnings("deprecation")
public
class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileUploadService fileUploadService;

    @Test
    void testUploadTextFile() throws Exception {
        // Given
        String content = "Hello World!\nThis is a test file.\nIt has multiple lines.";
        String filename = "test.txt";
        
        ProcessedFile processedFile = ProcessedFile.builder()
                .filename(filename)
                .lineCount(3)
                .wordCount(9)
                .processedTimestamp(LocalDateTime.now())
                .fileContent(content)
                .build();

        when(fileUploadService.processUploadedFile(any())).thenReturn(processedFile);

        MockMultipartFile file = new MockMultipartFile(
                "file", filename, "text/plain", content.getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/api/files/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("File uploaded and processed successfully"))
                .andExpect(jsonPath("$.filename").value(filename))
                .andExpect(jsonPath("$.lineCount").value(3))
                .andExpect(jsonPath("$.wordCount").value(9));
    }

    @Test
    void testUploadCsvFile() throws Exception {
        // Given
        String content = "Name,Age\nJohn,25\nJane,30";
        String filename = "test.csv";
        
        ProcessedFile processedFile = ProcessedFile.builder()
                .filename(filename)
                .lineCount(3)
                .wordCount(6)
                .processedTimestamp(LocalDateTime.now())
                .fileContent(content)
                .build();

        when(fileUploadService.processUploadedFile(any())).thenReturn(processedFile);

        MockMultipartFile file = new MockMultipartFile(
                "file", filename, "text/csv", content.getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/api/files/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("File uploaded and processed successfully"))
                .andExpect(jsonPath("$.filename").value(filename))
                .andExpect(jsonPath("$.lineCount").value(3))
                .andExpect(jsonPath("$.wordCount").value(6));
    }
}
