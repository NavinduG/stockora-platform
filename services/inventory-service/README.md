# Inventory Service

Part of the [Stockora](../../README.md) platform. Manages product inventory — catalog, stock levels, and stock operations.

## Tech Stack

- Java 21, Spring Boot 4
- Spring Data JPA + PostgreSQL
- Spring Boot Actuator (health/metrics)
- Docker (multi-stage build)
- JUnit 5, Mockito, Testcontainers

## API Endpoints

| Method | Endpoint | Description | Request Body |
|---|---|---|---|
| GET | `/products` | List all products | — |
| GET | `/products/{id}` | Get a product by ID | — |
| POST | `/products` | Create a new product | `{ "sku": "string", "name": "string", "quantity": int, "price": double }` |
| PUT | `/products/{id}/stock?quantity={n}` | Update stock quantity | — |
| DELETE | `/products/{id}` | Delete a product | — |

### Example: Create a product

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"sku":"LAPTOP-001","name":"ThinkPad X1","quantity":10,"price":1299.99}'
```

## Running Locally

Requires Java 21+, Maven, and a PostgreSQL database (this project uses a free [Supabase](https://supabase.com) instance).

```bash
export DB_HOST=<your-db-host>
export DB_PORT=5432
export DB_NAME=postgres
export DB_USER=<your-db-user>
export DB_PASSWORD=<your-db-password>

./mvnw spring-boot:run
```

App runs on `http://localhost:8080`.

## Running with Docker

```bash
docker build -t stockora-inventory-service .
docker run -p 8080:8080 \
  -e DB_HOST=<your-db-host> \
  -e DB_PORT=5432 \
  -e DB_NAME=postgres \
  -e DB_USER=<your-db-user> \
  -e DB_PASSWORD=<your-db-password> \
  stockora-inventory-service
```

## Running Tests

```bash
# Unit tests only
./mvnw test

# Unit + integration tests (spins up a real Postgres container via Testcontainers)
./mvnw verify
```

Integration tests require Docker to be available in the environment.

## CI/CD

On every push to `services/inventory-service/**`, GitHub Actions automatically builds the project and runs the full test suite (unit + integration). See [`.github/workflows/inventory-service-ci.yml`](../../.github/workflows/inventory-service-ci.yml).

## Health & Monitoring

Spring Boot Actuator exposes health and metrics endpoints:

- `GET /actuator/health`
- `GET /actuator/info`
- `GET /actuator/metrics`# Inventory Service
