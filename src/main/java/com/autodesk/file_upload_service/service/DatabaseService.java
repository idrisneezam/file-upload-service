package com.autodesk.file_upload_service.service;

import com.autodesk.file_upload_service.domain.ProcessedFile;

import java.util.Optional;

public interface DatabaseService {
    
    /**
     * Creates a new file entry in the database
     * @param processedFile the processed file to create
     */
    void createFile(ProcessedFile processedFile);
    
    /**
     * Reads a file from the database by filename
     * @param filename the filename to look up
     * @return Optional containing the file if found, empty otherwise
     */
    Optional<ProcessedFile> readFile(String filename);
    
    /**
     * Updates an existing file entry in the database
     * @param processedFile the processed file to update
     */
    void updateFile(ProcessedFile processedFile);
    
    /**
     * Checks if a file exists in the database
     * @param filename the filename to check
     * @return true if the file exists, false otherwise
     */
    boolean fileExists(String filename);
}