# catalog-service

Catalog microservice for Atlas Commerce Platform.

## Features (Phase 3)
- CRUD for `Category`.
- CRUD for `Product`.
- Product search (`q` query parameter) with pagination.

## Run locally
```bash
mvn spring-boot:run
```

## Key endpoints
- `GET /api/v1/categories`
- `POST /api/v1/categories`
- `GET /api/v1/products?q=atlas&page=0&size=20`
- `POST /api/v1/products`
