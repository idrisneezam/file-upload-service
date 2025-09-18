# file-upload-service
online assignment for autodesk: file upload, processing and persistence to db

Assumptions
- I will have simple web UI available for users to upload files, and it calls the backend service.
- Maximum upload size is 10 MB (configurable in the application yaml config file).
- All uploaded files are treated as UTF-8 encoded.
- Filenames are sanitized to prevent path traversal and other security issues.
- A "word" is any sequence of characters separated by whitespace.
- Duplicate handling: the filename is the primary key; if a filename already exists in the database, the existing record is updated.
- Concurrency: the in-memory data store will be thread-safe to support simultaneous uploads.