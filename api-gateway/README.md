# api-gateway

Spring Cloud Gateway entrypoint for Atlas Commerce Platform.

## Features (Phase 2)

- Route forwarding to auth, catalog, inventory, and order services.
- JWT validation for protected endpoints.
- In-memory IP-based rate limiting.
- Correlation ID propagation via `X-Correlation-Id`.
