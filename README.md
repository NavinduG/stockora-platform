# Stockora

**Stockora** is a cloud-native e-commerce order & inventory management platform built with a microservices architecture. It's a portfolio project demonstrating backend engineering and DevOps practices end-to-end — from service design through CI/CD to cloud deployment.

## 🏗️ Architecture

Stockora is composed of independently deployable Spring Boot microservices, each owning its own database, communicating via REST (and event-driven messaging as the platform grows).


*(Order Service and Notification Service are planned — see Roadmap below)*

## 🧰 Tech Stack

| Layer | Technology |
|---|---|
| Language / Framework | Java 21, Spring Boot 4 |
| Database | PostgreSQL (hosted on Supabase) |
| Messaging | RabbitMQ *(planned)* |
| Containerization | Docker (multi-stage builds) |
| CI/CD | GitHub Actions |
| Testing | JUnit 5, Mockito, Testcontainers |
| Cloud Deployment | *(planned — Terraform + cloud VM)* |
| Monitoring | Spring Boot Actuator *(Prometheus/Grafana planned)* |

## 📦 Services

### [`inventory-service`](./services/inventory-service)
Manages product inventory — stock levels, product catalog, and stock reservation logic.

**Status:** ✅ Functional — CRUD API, tested, containerized, CI pipeline running unit + integration tests.

| Endpoint | Method | Description |
|---|---|---|
| `/products` | GET | List all products |
| `/products/{id}` | GET | Get a single product |
| `/products` | POST | Create a new product |
| `/products/{id}/stock` | PUT | Update stock quantity |
| `/products/{id}` | DELETE | Delete a product |

### `order-service` *(coming soon)*
Will handle order creation and lifecycle, calling Inventory Service to reserve stock and publishing events on order status changes.

### `notification-service` *(coming soon)*
Will consume order events and send notifications.

## ✅ Engineering Practices Demonstrated

- **Microservices architecture** — independent services, database-per-service pattern
- **Test pyramid** — fast unit tests (Mockito) + real integration tests (Testcontainers spinning up actual Postgres containers)
- **CI/CD** — automated build + test on every push via GitHub Actions
- **Containerization** — multi-stage Dockerfiles for small, production-ready images
- **Cloud-native development** — built entirely in GitHub Codespaces, connected to a managed cloud database (Supabase)

## 🚀 Running Locally

Each service has its own README with setup instructions. Quick start for Inventory Service:

```bash
cd services/inventory-service
export DB_HOST=<your-db-host>
export DB_PORT=5432
export DB_NAME=postgres
export DB_USER=<your-db-user>
export DB_PASSWORD=<your-db-password>
./mvnw spring-boot:run
```

Or with Docker:

```bash
cd services/inventory-service
docker build -t stockora-inventory-service .
docker run -p 8080:8080 -e DB_HOST=... -e DB_PASSWORD=... stockora-inventory-service
```

## 🗺️ Roadmap

- [x] Inventory Service — CRUD API
- [x] Dockerize Inventory Service
- [x] CI pipeline (unit + integration tests)
- [ ] Order Service
- [ ] RabbitMQ event-driven communication between services
- [ ] CD — auto-publish Docker images to GitHub Container Registry
- [ ] Deploy to cloud (Terraform + VM/ECS)
- [ ] Monitoring dashboard (Prometheus + Grafana)

## 📄 License

MIT
