# Atlas Commerce Platform

Phase 1 foundation scaffold for a microservices e-commerce backend.

## Repository layout

- `api-gateway`, `auth-service`, `catalog-service`, `inventory-service`, `order-service`, `notification-service`
- `common-lib` shared Java library for errors, event contracts, and common configuration.
- `infrastructure` and `observability` directory scaffolding.
- `docs` for architecture and API documentation.

## Local infrastructure

Start dependencies with Docker Compose:

```bash
docker compose up -d
```

Provisioned services:

- PostgreSQL instances for auth, catalog, inventory, and order services.
- MongoDB instance for the notification service.
- Apache Kafka (KRaft mode) for async messaging.

Stop everything:

```bash
docker compose down -v
```
