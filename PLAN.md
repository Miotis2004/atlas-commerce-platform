# Atlas Commerce Platform - Development Plan

This document outlines the step-by-step development plan for the Atlas Commerce Platform based on the requirements defined in `DEVELOPMENT.md`.

## Phase 1: Foundation and Infrastructure Setup
**Objective:** Establish the foundational infrastructure, common libraries, and local development environment using Docker Compose.

1. **Initialize Repository Structure:**
   - Create directories for all microservices (`api-gateway`, `auth-service`, `catalog-service`, `inventory-service`, `order-service`, `notification-service`).
   - Create directories for `common-lib`, `infrastructure`, `observability`, and `docs`.
2. **Setup Docker Compose for Local Infrastructure:**
   - Configure PostgreSQL databases (one per relational service).
   - Configure MongoDB (for notification service).
   - Configure Apache Kafka (including Zookeeper or KRaft) for event messaging.
3. **Create Common Library (`common-lib`):**
   - Implement base exception handling.
   - Define common event schemas (e.g., `OrderCreated`, `StockReserved`).
   - Add standard configurations (e.g., JSON serialization, correlation ID propagation).

## Phase 2: Gateway and Authentication
**Objective:** Implement the entry point of the system and secure it with JWT-based authentication.

1. **Develop Auth Service (`auth-service`):**
   - Initialize Spring Boot 3.x with Java 21.
   - Implement user registration and login endpoints.
   - Setup PostgreSQL database with Flyway/Liquibase for `User` and `Role` entities.
   - Implement BCrypt password hashing and JWT generation.
   - Add role-based access control (USER, ADMIN).
2. **Develop API Gateway (`api-gateway`):**
   - Set up Spring Cloud Gateway.
   - Configure request routing to microservices.
   - Implement JWT validation and authentication enforcement.
   - Add rate limiting and correlation ID injection.

## Phase 3: Core Catalog and Inventory
**Objective:** Build out the catalog and inventory capabilities.

1. **Develop Catalog Service (`catalog-service`):**
   - Initialize service and connect to its dedicated PostgreSQL database.
   - Implement CRUD operations for `Product` and `Category`.
   - Add product search and pagination capabilities.
2. **Develop Inventory Service (`inventory-service`):**
   - Initialize service and connect to its dedicated PostgreSQL database.
   - Implement endpoints to manage `InventoryItem`.
   - Implement logic to reserve and release stock (`StockReservation`).
   - Setup Kafka consumers to listen to events (e.g., `OrderCreated`).

## Phase 4: Order Management and Messaging
**Objective:** Complete the core e-commerce flow by introducing order management and event-driven notifications.

1. **Develop Order Service (`order-service`):**
   - Initialize service and connect to its dedicated PostgreSQL database.
   - Implement logic to create orders (`Order`, `OrderItem`).
   - Publish domain events to Kafka (`OrderCreated`, `OrderCompleted`, `OrderCancelled`).
   - Implement idempotency keys for order creation endpoints.
2. **Integrate Event-Driven Architecture:**
   - Ensure `inventory-service` correctly consumes `OrderCreated` and produces `StockReserved` / `StockRejected`.
   - Ensure `order-service` updates state based on inventory events.
   - Configure Dead Letter Topics for failed messages.
3. **Develop Notification Service (`notification-service`):**
   - Initialize service and connect to MongoDB.
   - Consume various system events and generate mock notifications (Email, SMS, Audit logs).

## Phase 5: Observability and Monitoring
**Objective:** Integrate metrics, logging, and health checks across all services.

1. **Implement Spring Boot Actuator:**
   - Expose `/actuator/health`, `/actuator/prometheus`, and `/actuator/metrics` endpoints on all microservices.
2. **Setup Prometheus and Grafana:**
   - Add Prometheus configuration to scrape all service metrics.
   - Add Grafana to Docker Compose.
   - Create Grafana dashboards for request latency, error rates, Kafka lag, and JVM metrics.
3. **Enhance Logging:**
   - Configure structured JSON logging for all services.
   - Ensure `traceId` and `spanId` are propagated and included in all logs.

## Phase 6: CI/CD Pipeline and Production Readiness
**Objective:** Automate building, testing, and validation through GitHub Actions.

1. **Write Automated Tests:**
   - Ensure all services have comprehensive JUnit 5 and Mockito unit tests.
   - Add integration tests using Testcontainers (for PostgreSQL, MongoDB, Kafka).
2. **Create GitHub Actions Workflow:**
   - Configure pipeline to compile code, run tests, and build Docker images.
   - Add a step to run `docker-compose up` as a smoke test.
   - Ensure pipeline strictly fails on any step failure.
3. **Final Review & Documentation Update:**
   - Verify all APIs comply with JSON error format and pagination standards.
   - Update `docs` directory with generated architecture diagrams and API specifications.
