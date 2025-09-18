
# Features

- **File Upload**: Accept file uploads via REST API
- **File Validation**: Validate file types (.txt, .csv) and size limits
- **File Processing**: Count lines and words in uploaded files
- **Concurrent Database**: Thread-safe in-memory storage with read/write locks
- **Comprehensive Logging**: Detailed logging for monitoring and debugging
- **Exception Handling**: Global exception handling with meaningful error messages
- **REST API**: Full CRUD operations for file management


# API Endpoints

### Upload File
```
POST /api/files/upload
Content-Type: multipart/form-data
Body: file (multipart file)
```

**Response:**
```json
{
  "message": "File uploaded and processed successfully",
  "filename": "example.txt",
  "lineCount": 5,
  "wordCount": 25,
  "processedAt": "2024-01-01T12:00:00"
}
```

# Configs

The service is configured via `application.properties`:

- File upload limits: 10MB max file size
- Logging levels and patterns
- Server port: 8080
- Actuator endpoints for monitoring

# Usage

Starting the Application
```bash
mvn spring-boot:run
```

### Testing with curl

**Upload a text file:**
```bash
curl -X POST -F "file=@test-file.txt" http://localhost:8080/api/files/upload
```

**Upload a CSV file:**
```bash
curl -X POST -F "file=@test-file.csv" http://localhost:8080/api/files/upload
```

# File Validation

- **Allowed file types**: .txt, .csv
- **Maximum file size**: 10MB (configurable via application.properties)
- **Content type validation**: text/plain, text/csv, application/csv
- **Security checks**: Filename validation to prevent path traversal

# Database

- **In-memory storage**: Fast access using ConcurrentHashMap (no external database required)
- **Concurrent access**: Thread-safe operations using ReadWriteLock
- **Operations**: Create, Read, Update files only
- **Upsert logic**: Automatically creates new entries or updates existing ones based on filename
- **No persistence**: Data is lost when the application restarts (by design for this service)

# Dependencies

- **Spring Boot 3.5.5** - Main framework
- **Spring Web** - REST API endpoints
- **Spring Validation** - Input validation
- **Spring Actuator** - Health checks and monitoring
- **Lombok** - Reduces boilerplate code

# Monitoring

The service includes Spring Actuator endpoints:
- `/actuator/health`: Application health check
- `/actuator/info`: Application information
- `/actuator/metrics`: Application metrics

# Assumptions
- I will have simple web UI available for users to upload files, and it calls the backend service.
- Maximum upload size is 10 MB (configurable in the application properties config file).
- All uploaded files are treated as UTF-8 encoded.
- Filenames are sanitized to prevent path traversal and other security issues.
- A "word" is any sequence of characters separated by whitespace.
- Duplicate handling: the filename is the primary key; if a filename already exists in the database, the existing record is updated. Upsert will be used for all file uploads.
- Concurrency: the in-memory data store will be thread-safe to support simultaneous uploads.