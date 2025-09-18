package com.autodesk.file_upload_service.service.impl;

import com.autodesk.file_upload_service.domain.ProcessedFile;
import com.autodesk.file_upload_service.service.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Service
public class DatabaseServiceImpl implements DatabaseService {
    
    private final Map<String, ProcessedFile> fileStorage = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    @Override
    public void createFile(ProcessedFile processedFile) {
        if (processedFile == null) {
            throw new IllegalArgumentException("ProcessedFile cannot be null");
        }
        
        if (processedFile.getFilename() == null || processedFile.getFilename().trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
        
        lock.writeLock().lock();
        try {
            log.info("Creating new file entry in database: {}", processedFile.getFilename());
            
            fileStorage.put(processedFile.getFilename(), processedFile);
            
            log.info("Successfully created file '{}' with {} lines and {} words", 
                processedFile.getFilename(), 
                processedFile.getLineCount(), 
                processedFile.getWordCount());
                
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<ProcessedFile>  readFile(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            log.warn("Filename is null or empty for database lookup");
            return Optional.empty();
        }
        
        lock.readLock().lock();
        try {
            log.debug("Reading file from database: {}", filename);
            
            ProcessedFile file = fileStorage.get(filename);
            
            if (file != null) {
                log.debug("Found file '{}' in database", filename);
            } else {
                log.debug("File '{}' not found in database", filename);
            }
            
            return Optional.ofNullable(file);
            
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public void updateFile(ProcessedFile processedFile) {
        if (processedFile == null) {
            throw new IllegalArgumentException("ProcessedFile cannot be null");
        }
        
        if (processedFile.getFilename() == null || processedFile.getFilename().trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
        
        lock.writeLock().lock();
        try {
            log.info("Updating file in database: {}", processedFile.getFilename());
            
            fileStorage.put(processedFile.getFilename(), processedFile);
            
            log.info("Successfully updated file '{}' with {} lines and {} words", 
                processedFile.getFilename(), 
                processedFile.getLineCount(), 
                processedFile.getWordCount());
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean fileExists(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return false;
        }
        
        lock.readLock().lock();
        try {
            boolean exists = fileStorage.containsKey(filename);
            log.debug("File '{}' exists in database: {}", filename, exists);
            return exists;
        } finally {
            lock.readLock().unlock();
        }
    }
}
