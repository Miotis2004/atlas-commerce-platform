# inventory-service

Inventory microservice for Atlas Commerce Platform.

## Features (Phase 3)
- Manage `InventoryItem` records by product.
- Reserve and release stock through explicit endpoints.
- Consume `atlas.order.created` Kafka events and attempt stock reservation.

## Run locally
```bash
mvn spring-boot:run
```

## Key endpoints
- `GET /api/v1/inventory/items`
- `POST /api/v1/inventory/items`
- `POST /api/v1/inventory/stock/reserve`
- `POST /api/v1/inventory/stock/release/{reservationKey}`
