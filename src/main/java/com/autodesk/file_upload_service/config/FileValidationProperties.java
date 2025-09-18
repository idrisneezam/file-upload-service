package com.autodesk.file_upload_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "file.validation")
public class FileValidationProperties {
    
    /**
     * Maximum file size in bytes (default: 10MB)
     */
    private long maxSize = 10485760L;
    
    /**
     * List of allowed file extensions
     */
    private List<String> allowedExtensions = List.of(".txt", ".csv");
    
    /**
     * List of allowed content types
     */
    private List<String> allowedContentTypes = List.of("text/plain", "text/csv", "application/csv");
}
