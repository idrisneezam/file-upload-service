package com.autodesk.file_upload_service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedFile {
    private String filename;
    private long lineCount;
    private long wordCount;
    private LocalDateTime processedTimestamp;
    private String fileContent;
}
