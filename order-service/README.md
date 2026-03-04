# order-service

Order management microservice for Atlas Commerce Platform.

## Features (Phase 4)
- Create and fetch orders (`Order`, `OrderItem`).
- Enforce idempotent order creation through `Idempotency-Key` header.
- Publish domain events: `atlas.order.created`, `atlas.order.completed`, `atlas.order.cancelled`.
- Consume stock events (`atlas.stock.reserved`, `atlas.stock.rejected`) to update order lifecycle.

## Run locally
```bash
mvn spring-boot:run
```
