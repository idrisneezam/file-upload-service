package com.autodesk.file_upload_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(FileValidationException.class)
    public ResponseEntity<Map<String, Object>> handleFileValidationException(FileValidationException e) {
        log.warn("File validation error: {}", e.getMessage());
        
        Map<String, Object> response = createErrorResponse(
            "File validation failed", 
            e.getMessage(), 
            HttpStatus.BAD_REQUEST
        );
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleFileProcessingException(FileProcessingException e) {
        log.error("File processing error: {}", e.getMessage(), e);
        
        Map<String, Object> response = createErrorResponse(
            "File processing failed", 
            e.getMessage(), 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.warn("File size exceeded limit: {}", e.getMessage());
        
        Map<String, Object> response = createErrorResponse(
            "File size exceeded", 
            "The uploaded file exceeds the maximum allowed size", 
            HttpStatus.PAYLOAD_TOO_LARGE
        );
        
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Invalid argument: {}", e.getMessage());
        
        Map<String, Object> response = createErrorResponse(
            "Invalid argument", 
            e.getMessage(), 
            HttpStatus.BAD_REQUEST
        );
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        
        Map<String, Object> response = createErrorResponse(
            "Internal server error", 
            "An unexpected error occurred while processing your request", 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    private Map<String, Object> createErrorResponse(String error, String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        response.put("message", message);
        response.put("status", status.value());
        response.put("timestamp", LocalDateTime.now());
        return response;
    }
}
