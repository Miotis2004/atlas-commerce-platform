# auth-service

Spring Boot authentication service for Atlas Commerce Platform.

## Features (Phase 2)

- User registration and login endpoints (`/api/v1/auth/register`, `/api/v1/auth/login`).
- PostgreSQL persistence with Flyway schema migration.
- BCrypt password hashing.
- JWT generation with `USER` and `ADMIN` roles.
- Sample role-protected endpoint (`/api/v1/admin/ping`).
