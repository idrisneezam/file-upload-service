package com.autodesk.file_upload_service.service;

import com.autodesk.file_upload_service.domain.ProcessedFile;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    
    /**
     * Processes an uploaded file and returns the processed result
     * @param file the uploaded multipart file
     * @return ProcessedFile containing the processing results
     */
    ProcessedFile processUploadedFile(MultipartFile file);
}
