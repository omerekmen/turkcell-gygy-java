<h1 align="center">
  <img src="https://readme-typing-svg.demolab.com?font=JetBrains+Mono&weight=600&size=28&duration=3000&pause=1000&color=B8D8B0&center=true&vCenter=true&random=false&width=500&lines=%C3%96mer+Ekmen;Software+Engineer;Mathematics+Graduate" alt="Typing SVG" />
</h1>

<p align="center">
  <a href="https://www.omerekmen.com"><img src="https://img.shields.io/badge/Portfolio-omerekmen.com-B8D8B0?style=for-the-badge&logo=safari&logoColor=white" alt="Portfolio" /></a>
  <a href="https://linkedin.com/in/omerekmenn"><img src="https://img.shields.io/badge/LinkedIn-omerekmenn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn" /></a>
  <a href="mailto:omerekmenn@gmail.com"><img src="https://img.shields.io/badge/Email-omerekmenn-EA4335?style=for-the-badge&logo=gmail&logoColor=white" alt="Email" /></a>
</p>

---

# Turkcell GYGY 5.0 Java Practices

This repository includes Java exercises and mini-applications created during the **Turkcell GYGY 5.0 Java** training program.

## Workspace Layout

```text

TURKCELL GYGY/
  └─ turkcell-gygy-java/
    └─ banking-app/
    └─ spring-cqrs/
    └─ library-cqrs/
```

## Banking App Summary

The banking app is a Maven-based Java 21 console application with:

- user login and registration by TCKN
- in-memory user and bank account repositories
- account detail viewing
- deposit, withdrawal, and transfer flows
- mock data loaded at startup for demo use

## Build And Run

From the app folder:

```bash
cd TURKCELL\ GYGY/turkcell-gygy-java/banking-app
mvn clean compile
java -cp target/classes com.banking.Main
```

The included [Makefile](TURKCELL%20GYGY/turkcell-gygy-java/banking-app/Makefile) also provides:

- `make compile`
- `make runclass`
- `make full`

## Spring CQRS Project

A modern Spring Boot 3 REST API implementing the CQRS pattern with JWT-based authentication and role-based access control (RBAC).

### Recent Updates (May 13, 2026)

#### Authentication & Authorization Framework
- **JWT Authentication**: Stateless token-based authentication with JwtService and JwtAuthFilter
- **Role-Based Access Control**: Database-backed roles with User-Role ManyToMany relation
- **Security Pipeline**: AuthorizationBehavior enforces authentication and role requirements at the request pipeline level

#### Exception Handling
- **UnauthenticatedException** (401) — User not authenticated
- **UnauthorizedException** (403) — User lacks required role
- **Global Exception Handler** with database persistence to ExceptionLog table

#### Database Models
- **Role Entity**: Centralized role definitions (USER, ADMIN, etc.)
- **User-Role Mapping**: ManyToMany join table for flexible role assignment
- **Role Seeding**: Initial roles (USER, ADMIN) auto-populated in data.sql

#### Feature Implementation
- **CreateCategoryCommand** now requires ADMIN role (demo of RBAC)
- **Registration Flow** assigns default USER role automatically
- **Login** generates JWT with role claims from database
- **UserMapper**: Cleaned up register/response mapping logic

#### Code Quality
- Null-safe role extraction and validation
- Type-safe JWT role claim casting
- Separation of concerns via mapper pattern
- Request-scoped UserContext for authenticated user info

### Quick Start

```bash
cd TURKCELL\ GYGY/turkcell-gygy-java/spring-cqrs
mvn clean compile
mvn spring-boot:run
```

For detailed API documentation and role assignment, see [spring-cqrs/README.md](spring-cqrs/README.md).

## Notes

- The banking app stores data only in memory, so all state resets when the program exits.
- Passwords are stored in plain text in the current training implementation.
- The root of this workspace is mainly for coordination and reference across projects.
