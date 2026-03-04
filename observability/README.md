# Observability Stack

Phase 5 observability assets for Atlas Commerce Platform.

## Included components

- **Prometheus** scrape configuration for all service actuator endpoints (`/actuator/prometheus`).
- **Grafana** provisioning for:
  - Prometheus datasource
  - Auto-loaded `Atlas Platform Overview` dashboard with latency, error-rate, Kafka lag, and JVM memory charts.

## Start stack

```bash
docker compose up -d prometheus grafana
```

Grafana is available at `http://localhost:3000` (`atlas` / `atlas`).
