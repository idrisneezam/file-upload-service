package com.autodesk.file_upload_service.service;

import com.autodesk.file_upload_service.domain.ProcessedFile;

public interface FileProcessingService {
    
    /**
     * Processes a file and returns the processed result
     * @param fileContent the content of the file to process
     * @param filename the name of the file
     * @return ProcessedFile containing the processing results
     */
    ProcessedFile processFile(String fileContent, String filename);
    
    /**
     * Counts the number of lines in the given content
     * @param content the content to count lines in
     * @return the number of lines
     */
    long countLines(String content);
    
    /**
     * Counts the number of words in the given content
     * @param content the content to count words in
     * @return the number of words
     */
    long countWords(String content);
}