# API Result and Exception Handling Guide

## Overview

This guide explains the new exception handling and API result system implemented for the library-app. The system provides:

1. **Consistent API Responses** - All endpoints return a standardized `ApiResult` wrapper
2. **Comprehensive Exception Logging** - All exceptions are logged to the database with full context
3. **Developer-Friendly Error Tracking** - Error log IDs are returned to clients for debugging

## ApiResult Structure

### Successful Response Example
```json
{
  "success": true,
  "statusCode": 200,
  "message": "Book retrieved successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "title": "Clean Code",
    "isbn": "978-0132350884"
  },
  "logId": null,
  "timestamp": "2026-04-29T15:30:00"
}
```

### Error Response Example
```json
{
  "success": false,
  "statusCode": 400,
  "message": "Validation failed: title: must not be blank",
  "data": null,
  "logId": "a1b2c3d4-e5f6-47a8-9b0c-1d2e3f4a5b6c",
  "timestamp": "2026-04-29T15:30:00"
}
```

## Using ApiResult in Controllers

### Basic Usage - Success Response
```java
@RestController
@RequestMapping("/api/v1/books")
public class BooksController {
    private final BookService bookService;
    
    @GetMapping("/{id}")
    public ApiResult<BookResponse> getBook(@PathVariable UUID id) {
        BookResponse book = bookService.getById(id);
        return ApiResult.success("Book retrieved successfully", book);
    }
}
```

### With Custom Status Codes
```java
@PostMapping
public ApiResult<BookResponse> createBook(@RequestBody CreateBookRequest request) {
    BookResponse book = bookService.create(request);
    return ApiResult.success(
        201,  // CREATED
        "Book created successfully",
        book
    );
}
```

### Response Without Data
```java
@DeleteMapping("/{id}")
public ApiResult<Void> deleteBook(@PathVariable UUID id) {
    bookService.delete(id);
    return ApiResult.success(204, "Book deleted successfully");
}
```

## Exception Handling

The `GlobalExceptionHandler` now handles various exception types:

### Validation Errors (400)
When using `@Valid` annotation:
```java
@PostMapping
public ApiResult<BookResponse> create(
    @Valid @RequestBody CreateBookRequest request
) {
    return ApiResult.success("Book created", bookService.create(request));
}
```

**Response:**
```json
{
  "success": false,
  "statusCode": 400,
  "message": "Validation failed: title: must not be blank, isbn: must not be blank",
  "logId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2026-04-29T15:30:00"
}
```

### Data Integrity Violations (409)
Example: Duplicate ISBN
```json
{
  "success": false,
  "statusCode": 409,
  "message": "A duplicate entry already exists. Please check your input.",
  "logId": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp": "2026-04-29T15:30:00"
}
```

### Resource Not Found (404)
Automatically handled for non-existent endpoints.

### Type Mismatch (400)
Invalid UUID format or type conversion errors.

### Database Errors (500)
Database connection or access errors.

## Retrieving Exception Logs

Developers can retrieve full exception details using the log ID:

### API Endpoint
```
GET /api/v1/exception-logs/{logId}
```

### Example Request
```bash
curl -X GET \
  "http://localhost:8080/api/v1/exception-logs/550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json"
```

### Example Response
```json
{
  "success": true,
  "statusCode": 200,
  "message": "Exception log retrieved successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "exceptionType": "org.springframework.web.bind.MethodArgumentNotValidException",
    "message": "Validation failed",
    "stackTrace": "...",
    "method": "POST",
    "uri": "/api/v1/books",
    "queryParams": null,
    "userInfo": "admin",
    "clientIp": "192.168.1.100",
    "statusCode": 400,
    "createdAt": "2026-04-29T15:30:00",
    "context": "Validation Error: title: must not be blank"
  },
  "timestamp": "2026-04-29T15:30:00"
}
```

## Database Schema

### ExceptionLog Table
```sql
CREATE TABLE exception_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    exception_type VARCHAR(255) NOT NULL,
    message TEXT,
    stack_trace TEXT NOT NULL,
    method VARCHAR(10),
    uri TEXT,
    query_params TEXT,
    request_body TEXT,
    user_info VARCHAR(255),
    client_ip VARCHAR(45),
    status_code INTEGER,
    context TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Indexes
- `idx_exception_logs_created_at` - Query logs by creation time
- `idx_exception_logs_exception_type` - Filter by exception type
- `idx_exception_logs_status_code` - Filter by HTTP status
- `idx_exception_logs_user_info` - Filter by user
- `idx_exception_logs_client_ip` - Filter by client IP

## Best Practices

### 1. Always Use ApiResult
```java
// Good
return ApiResult.success("Operation completed", data);

// Avoid
return data;
```

### 2. Use Appropriate Status Codes
```java
// 201 for creation
return ApiResult.success(201, "Resource created", newResource);

// 204 for deletion
return ApiResult.success(204, "Resource deleted");

// 200 for retrieval/update
return ApiResult.success("Data retrieved", data);
```

### 3. Handle Custom Exceptions
```java
// In GlobalExceptionHandler
@ExceptionHandler({CustomBusinessException.class})
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ApiResult<?> handleCustomBusinessException(
        CustomBusinessException ex,
        HttpServletRequest request) {
    
    ExceptionLog log = exceptionLogService.logException(ex, request, "Custom business error");
    
    return ApiResult.error(
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage(),
        log.getId().toString()
    );
}
```

### 4. Validate Input
```java
@PostMapping
public ApiResult<BookResponse> create(
    @Valid @RequestBody CreateBookRequest request
) {
    // Validation happens automatically, exception handler catches it
    return ApiResult.success(201, "Book created", bookService.create(request));
}
```

### 5. Search Exception Logs
Use the exception log endpoint to retrieve and analyze errors:
```bash
# Get specific error log
curl -X GET "http://localhost:8080/api/v1/exception-logs/{logId}"

# Parse the stack trace and context to understand the issue
```

## Dependencies Added

The following dependencies were added to `pom.xml`:
- **Lombok** - For reducing boilerplate code (@Data, @Builder, etc.)
- **SLF4J with Logback** - Already included via Spring Boot starter

## Migration Guide

### Updating Existing Controllers

**Before:**
```java
@GetMapping("/{id}")
public BookResponse getById(@PathVariable UUID id) {
    return bookService.getById(id);
}
```

**After:**
```java
@GetMapping("/{id}")
public ApiResult<BookResponse> getById(@PathVariable UUID id) {
    BookResponse book = bookService.getById(id);
    return ApiResult.success("Book retrieved successfully", book);
}
```

The exception handler will automatically catch any exceptions and return proper error responses with log IDs.

## Monitoring

### View Recent Errors
Query the exception_logs table directly:
```sql
SELECT * FROM exception_logs 
ORDER BY created_at DESC 
LIMIT 10;
```

### Find Errors by User
```sql
SELECT * FROM exception_logs 
WHERE user_info = 'username' 
ORDER BY created_at DESC;
```

### Find Errors by Endpoint
```sql
SELECT * FROM exception_logs 
WHERE uri LIKE '%/books%' 
ORDER BY created_at DESC;
```

### Error Statistics
```sql
SELECT exception_type, COUNT(*) as count, 
       MAX(created_at) as latest
FROM exception_logs 
GROUP BY exception_type 
ORDER BY count DESC;
```
