# API Specifications

## Platform Standards

- **Authentication:** JWT bearer token required for protected routes.
- **Error format:** JSON error envelope with timestamp, status, error, message, and path.
- **Pagination:** `page`, `size`, and stable sorting parameters on list endpoints.
- **Tracing:** Correlation IDs propagated through gateway and service boundaries.

## Service Endpoint Catalog

### Auth Service
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/admin/health` (role-restricted sample)

### Catalog Service
- `GET /api/v1/products`
- `GET /api/v1/products/{id}`
- `POST /api/v1/products`
- `PUT /api/v1/products/{id}`
- `DELETE /api/v1/products/{id}`
- `GET /api/v1/categories`
- `POST /api/v1/categories`

### Inventory Service
- `GET /api/v1/inventory`
- `POST /api/v1/inventory`
- `POST /api/v1/stock/reserve`
- `POST /api/v1/stock/release`

### Order Service
- `POST /api/v1/orders`
- `GET /api/v1/orders/{id}`
- `GET /api/v1/orders`

### Notification Service
- Event-driven consumer endpoints via Kafka listeners for order and stock domain events.
