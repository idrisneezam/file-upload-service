package com.autodesk.file_upload_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "file.processing")
public class FileProcessingProperties {
    
    /**
     * Regex pattern for word counting (default: \b\w+\b)
     */
    private String wordPattern = "\\b\\w+\\b";
    
    /**
     * Regex pattern for line counting (default: \r?\n)
     */
    private String linePattern = "\\r?\\n";
}
