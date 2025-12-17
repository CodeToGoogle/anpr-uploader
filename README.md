# ANPR Excel Uploader - Spring Boot

This is a minimal Spring Boot project skeleton that provides an endpoint to upload Excel files containing ANPR "Details" text blocks and stores parsed events into a MySQL database.

Important: **Snap** column is ignored; images are not stored.

## What is included
- REST endpoint: `POST /api/excel/upload-events` accepts `multipart/form-data` with `file` field.
- Excel parser using Apache POI; parses the **Details** column (assumed in column B).
- Duplicate prevention using unique constraint on (camera_name, event_time, plate_number).
- JPA entities and repository.

## How to run
1. Configure MySQL in `src/main/resources/application.properties`.
2. Create database (see `schema.sql`).
3. Build:
   ```
   mvn clean package
   java -jar target/anpr-uploader-0.0.1-SNAPSHOT.jar
   ```
4. Upload using curl or Postman:
   ```
   curl -F file=@events.xlsx http://localhost:8080/api/excel/upload-events
   ```

## Notes
- This is a starter project: you should add proper security, testing, logging, and advanced error handling for production.
