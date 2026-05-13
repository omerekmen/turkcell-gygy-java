# Spring CQRS Project

A clean architecture implementation of a Spring Boot application using CQRS pattern with authentication, authorization, and role-based access control.

## Project Structure

```
src/main/java/com/turkcell/spring_cqrs/
├── application/          # Application layer (use cases, commands, queries, mappers)
├── domain/               # Domain layer (entities, business rules)
├── infrastructure/       # Infrastructure layer (persistence, logging)
├── persistence/          # Repository interfaces
├── core/                 # Core cross-cutting concerns (mediator, security, exception handling)
└── web/                  # Web layer (REST controllers)
```

## Tech Stack

- **Java 21**
- **Spring Boot 3.x**
- **PostgreSQL** (default DB)
- **JPA/Hibernate** for ORM
- **JWT** for authentication
- **CQRS** pattern for command/query separation
- **Spring Security** for password encoding
- **Maven** for build

## Features

### Authentication & Authorization
- JWT-based stateless authentication
- Role-based access control (RBAC)
- DB-backed role management with `User-Role` ManyToMany relation
- Exception-based security enforcement with clear HTTP status codes

### CQRS Architecture
- Command handlers for write operations
- Query handlers for read operations
- Pipeline behaviors for cross-cutting concerns (logging, performance monitoring, authorization)

### API Exceptions
- `UnauthenticatedException` → 401 Unauthorized
- `UnauthorizedException` → 403 Forbidden
- Global exception handler with persistence to DB

## Changelog

### Latest Updates (Session: May 13, 2026)

#### 1. Exception Handling & Security Foundation
- **Created**: `UnauthenticatedException` and `UnauthorizedException` classes
- **Updated**: `GlobalExceptionHandler` to map these exceptions to proper HTTP status codes (401, 403)
- **Updated**: Exception persistence to log all errors to `ExceptionLog` table

#### 2. Authentication Pipeline
- **Refactored**: `AuthorizationBehavior` to enforce authentication checks
- **Result**: All requests implementing `AuthorizableRequest` now require valid JWT authentication
- **Made Authenticatable**: `CreateCategoryCommand` now implements `AuthorizableRequest`

#### 3. Role-Based Access Control (RBAC)
- **Enhanced**: `AuthorizableRequest` interface with `default List<String> requiredRoles()` method
- **Updated**: `AuthorizationBehavior` to enforce role checks on authenticated requests
- **Result**: Requests can declare required roles; mismatches throw `UnauthorizedException` (403)
- **Configured**: `CreateCategoryCommand` requires `ADMIN` role as demo

#### 4. Database-Backed Roles
- **Created**: `Role` entity with `id` and `name` fields
- **Created**: `RoleRepository` for role persistence and lookup
- **Refactored**: `User` entity
  - Replaced string `roles` field with `@ManyToMany` relation to `Role`
  - Uses join table `user_roles` (`user_id`, `role_id`)
  - Eager loading ensures roles are available in all contexts

#### 5. JWT Role Integration
- **Updated**: `JwtService.generate()` with overload accepting `List<String> roles`
- **Enhanced**: `JwtService.generate()` includes roles as JWT claim
- **Hardened**: `extractRoles()` with null-safety and type-safe casting to prevent JWT role extraction errors
- **Updated**: `LoginCommandHandler` to extract roles from `User.roles` and include them in JWT

#### 6. Registration Flow with Default Roles
- **Updated**: `RegisterCommandHandler` to:
  - Look up "USER" role from DB (creates if missing)
  - Assign default `USER` role to newly registered users
  - Injected `RoleRepository` for role management

#### 7. Data Seeding
- **Updated**: `data.sql` to seed initial roles:
  - `USER` (aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa)
  - `ADMIN` (bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb)

#### 8. Null-Safety Enhancements
- **Hardened**: `UserContext` to handle null roles gracefully
- **Type-Safe**: JWT role extraction with proper casting and filtering
- **Defensive**: Login handler role mapping with null checks and trim operations

#### 9. Mapper Implementation
- **Created**: `UserMapper` for user-related mappings
  - `userFromRegisterCommand()` — maps `RegisterCommand` + encoded password + roles to `User`
  - `registerResponseFromUser()` — maps `User` to `RegisterResponse`
- **Refactored**: `RegisterCommandHandler` to use `UserMapper`
  - Removed inline `User` construction
  - Removed inline `RegisterResponse` construction
  - Cleaner separation of concerns

## Database Setup

The application uses PostgreSQL with Hibernate ORM in update mode (`ddl-auto: update`).

### Initial Tables (Auto-created)
- `users` — User accounts with email and encrypted password
- `roles` — Role definitions
- `user_roles` — ManyToMany mapping between users and roles
- `categories` — Product categories
- `tags` — Product tags
- `products` — Products linked to categories
- `product_tags` — Product-tag mappings
- `exception_logs` — Exception audit trail

### Configuration
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
  datasource:
    url: jdbc:postgresql://0.0.0.0:5432/ecommerce
    username: admin
    password: turkcell-gygy-admin
```

## API Examples

### Register User
```bash
POST /api/auth/register
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```
Returns: `{ "id": "...", "email": "user@example.com" }`
Assigns default `USER` role automatically.

### Login
```bash
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```
Returns: `{ "accessToken": "eyJhbGciOiJIUzI1NiJ9..." }`

### Create Category (Requires ADMIN)
```bash
POST /api/categories
Headers: Authorization: Bearer <accessToken>
{
  "name": "Electronics"
}
```
Returns: `{ "id": "...", "name": "Electronics" }`
- If not authenticated → 401 Unauthenticated
- If authenticated but lacking `ADMIN` role → 403 Forbidden
- If authenticated with `ADMIN` → 200 OK

## Assignment Strategy

### How to Assign Roles in Database

#### Option 1: SQL (Development)
```sql
UPDATE user_roles SET role_id = 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb' 
WHERE user_id = '<user_uuid>';
```
(Role ID for `ADMIN`)

#### Option 2: Future Feature
Create an admin endpoint to assign roles (not yet implemented).

## Security Considerations

- Passwords are encrypted using Spring Security's `PasswordEncoder`
- JWT tokens include issuer and expiration claims
- All authentication/authorization failures are logged
- Role checks happen in the request pipeline before handler execution

## Building & Running

### Build
```bash
mvn clean install
```

### Compile
```bash
mvn clean compile
```

### Run
```bash
mvn spring-boot:run
```

## Notes

- The application uses request-scoped `UserContext` to store authenticated user info
- All requests are filtered through `JwtAuthFilter` to extract and validate JWT tokens
- Pipeline behaviors run in order: `LoggingBehavior` → `PerformanceBehavior` → `AuthorizationBehavior`
