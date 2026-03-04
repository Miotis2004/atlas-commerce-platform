DEVELOPMENT.md

Atlas Commerce Platform Development Guide

1. Project Overview

Atlas Commerce Platform is a production-style microservices backend system built using Java, Spring Boot, and event-driven architecture.

The project is designed to demonstrate enterprise backend engineering skills, including:

Spring Boot microservices

RESTful API design

secure authentication and authorization

event-driven architecture using Kafka

relational and NoSQL databases

Docker containerization

CI/CD pipelines

observability and monitoring

reliability and resilience patterns

The platform models a simplified e-commerce backend consisting of several independently deployable services.

The system must run entirely locally using Docker Compose, though it should be architected as if it could run in production cloud environments.

2. Core Architecture Principles

All development must follow these architectural rules.

Microservice Architecture

Each domain capability must exist as an independent microservice.

Each service must have:

its own database

its own data model

independent deployment

independent scaling

Services must never access another service's database directly.

Communication occurs through:

REST APIs

asynchronous events (Kafka)

Event-Driven Architecture

Domain events must be used to propagate system changes.

Examples:

OrderCreated
StockReserved
StockReleased
StockLow
OrderCompleted

Events must be:

immutable

versioned

idempotent

Domain Boundaries

Services must represent clear domain responsibilities.

Example:

Order service should not contain inventory logic.

Observability First

All services must expose:

metrics

logs

health endpoints

These must be available for scraping and aggregation.

3. Technology Stack

All services must use the following technologies.

Core Backend

Java 21

Spring Boot 3.x

Spring MVC

Spring Security

Spring Data JPA

Hibernate

Flyway or Liquibase for database migrations

Messaging

Apache Kafka

Spring Kafka

Dead Letter Topics must be supported.

Databases

Primary relational store:

PostgreSQL

Optional NoSQL read model:

MongoDB

Testing

JUnit 5

Mockito

Spring Boot Test

Testcontainers for integration testing

Containerization

Docker

Docker Compose

CI/CD

GitHub Actions

CI must perform:

build

unit tests

integration tests

Docker image build

Observability

Spring Boot Actuator

Prometheus

Grafana

Optional:

ELK stack or OpenSearch for logs

4. Repository Structure

The repository must follow this structure.

atlas-commerce-platform

api-gateway
auth-service
catalog-service
inventory-service
order-service
notification-service

common-lib

infrastructure
    docker
    kafka
    postgres
    monitoring
    terraform
    kubernetes

observability
    prometheus
    grafana
    logging

docs

docker-compose.yml
5. Microservices
API Gateway

Responsibilities

central entry point

request routing

authentication enforcement

rate limiting

correlation ID injection

Technology

Spring Cloud Gateway

Auth Service

Responsibilities

user registration

login

JWT token generation

role-based access control

Database

PostgreSQL

Entities

User
Role

Catalog Service

Responsibilities

product catalog

product search

pricing information

Database

PostgreSQL

Entities

Product
Category

Inventory Service

Responsibilities

track product stock

reserve stock for orders

release stock on cancellation

Database

PostgreSQL

Entities

InventoryItem
StockReservation

Events Consumed

OrderCreated

Events Produced

StockReserved
StockRejected

Order Service

Responsibilities

create orders

manage order lifecycle

publish order events

Database

PostgreSQL

Entities

Order
OrderItem

Events Produced

OrderCreated
OrderCompleted
OrderCancelled

Notification Service

Responsibilities

consume system events

generate notifications

Example outputs

Email
SMS
Audit logs

Database

MongoDB

6. API Standards

All REST APIs must follow these standards.

JSON Format

All responses must be JSON.

Error Format

Errors must follow this structure.

{
  "timestamp": "2026-01-01T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid order state",
  "path": "/orders"
}
Pagination

All list endpoints must support pagination.

Parameters

page
size
sort

Idempotency

Endpoints that create resources must support Idempotency-Key headers.

Versioning

APIs must be versioned.

Example

/api/v1/orders
7. Security Standards

Authentication

JWT Bearer Tokens

Authorization

Role-based access control

Roles

USER
ADMIN

Passwords must be stored using:

BCrypt hashing

8. Messaging Standards

Kafka topics must follow naming conventions.

Example

orders.created
inventory.reserved
inventory.failed
orders.completed
Event Schema Example

OrderCreated

{
  "eventId": "uuid",
  "eventType": "OrderCreated",
  "timestamp": "ISO-8601",
  "orderId": "uuid",
  "userId": "uuid",
  "items": [
    {
      "productId": "uuid",
      "quantity": 2
    }
  ]
}
Reliability Requirements

Consumers must be idempotent.

Retries must be supported.

Dead Letter Topics must exist.

9. Observability

Each service must expose:

/actuator/health
/actuator/prometheus
/actuator/metrics

Prometheus will scrape all services.

Grafana dashboards must visualize:

request latency

error rates

Kafka lag

JVM metrics

10. Logging

All logs must be structured JSON logs.

Required fields

timestamp
service
traceId
spanId
level
message

Correlation IDs must propagate across services.

11. CI/CD Pipeline

GitHub Actions pipeline must perform:

Compile code

Run unit tests

Run integration tests

Build Docker images

Start docker-compose smoke test

Pipeline must fail if any step fails.

12. Performance Requirements

All services must:

support connection pooling

use indexed database queries

avoid N+1 queries

Pagination must be used for large datasets.

13. Development Rules for Codex

Codex must follow these rules.

Always read DEVELOPMENT.md before generating code.

Do not change architecture decisions without updating this document.

Only modify files relevant to the requested task.

All code must compile.

Tests must accompany new functionality.

Services must run inside Docker containers.

Each feature must be deliverable as a small pull request.

14. Definition of Done

A feature is complete when:

code compiles

unit tests pass

integration tests pass

docker-compose environment runs successfully

documentation is updated

15. Local Development

The entire platform must run locally.

Start the system with:

docker compose up --build

Services must be reachable through the API gateway.

16. Future Enhancements (Optional)

These may be added after the core system is stable.

Kubernetes deployment

Terraform infrastructure

distributed tracing with OpenTelemetry

service mesh integration

caching layer (Redis)

17. Documentation

Additional documentation must be placed in:

/docs

Including

architecture diagrams

API specifications

runbooks

operational procedures

18. Goal of This Project

Atlas Commerce Platform exists to demonstrate the ability to build enterprise-grade distributed systems using modern Java and DevOps practices.

The codebase should resemble a real production engineering system, not a tutorial project.