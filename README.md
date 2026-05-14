# Quantix — Inventory Management API

> Spring Boot REST API for inventory management with full CRUD, search, filtering, low-stock detection, and pagination.

🔗 **Live API:** [https://quantix-7cav.onrender.com](https://quantix-7cav.onrender.com)
📋 **Jira Board:** [QUANTIX Project](https://mammadzadaaslan05.atlassian.net/jira/software/projects/QUANTIX/boards/34)

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Run Locally (Maven)](#run-locally-maven)
  - [Run with Docker](#run-with-docker)
- [Configuration](#configuration)
- [API Reference](#api-reference)
- [Error Responses](#error-responses)
- [CI/CD](#cicd)

---

## Features

- ✅ Full CRUD for inventory items
- 🔍 Search by name or SKU
- 🗂️ Filter by category
- ⚠️ Low stock detection
- 📄 Pagination and sorting
- 🛡️ Input validation and global exception handling

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL (prod) / H2 (test) |
| Build Tool | Maven |
| Utilities | Lombok |
| Containerization | Docker + Docker Compose |
| CI | GitHub Actions |

---

## Project Structure

```
src/main/java/org/example/quantix/
├── controller/
├── service/
├── repository/
├── mapper/
├── dto/
│   ├── request/
│   └── response/
├── entity/
└── exception/
```

---

## Getting Started

### Prerequisites

- Java 17
- Maven 3.9+
- MySQL 8.0 (or Docker)

### Run Locally (Maven)

```bash
# 1. Clone the repository
git clone https://github.com/URLeeo/Quantix.git
cd Quantix

# 2. Set up your database and update application.properties (see Configuration)

# 3. Build the project
mvn clean install

# 4. Run the application
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

### Run with Docker

Create a `.env` file in the project root:

```env
SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/quantixdb
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password
MYSQL_ROOT_PASSWORD=your_password
MYSQL_DATABASE=quantixdb
```

Then start the containers:

```bash
docker compose up --build
```

The app container waits for MySQL to be healthy before starting.

### Run Tests

```bash
mvn test
```

---

## Configuration

Key properties in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/quantixdb
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=${DDL_AUTO:update}
spring.jpa.show-sql=${SHOW_SQL:true}

server.port=${SERVER_PORT:8080}
```

A separate `application-prod.properties` and `application-test.properties` are included for environment-specific overrides.

---

## API Reference

**Base URL (production):** `https://quantix-7cav.onrender.com/api/v1/inventory-items`  
**Base URL (local):** `http://localhost:8080/api/v1/inventory-items`  
**Swagger UI:** [https://quantix-7cav.onrender.com/swagger-ui.html](https://quantix-7cav.onrender.com/swagger-ui.html)

---

### Create Item

```
POST /api/v1/inventory-items
```

**Request Body:**

```json
{
  "name": "Laptop",
  "sku": "LP-1001",
  "category": "Electronics",
  "quantity": 10,
  "price": 1500,
  "supplierName": "Tech Supplier"
}
```

---

### Get All Items

```
GET /api/v1/inventory-items
```

Supports pagination, sorting, search, and category filtering:

```
GET /api/v1/inventory-items?page=0&size=10&sort=createdAt,desc
GET /api/v1/inventory-items?search=laptop
GET /api/v1/inventory-items?category=Electronics
```

---

### Get Item by ID

```
GET /api/v1/inventory-items/{id}
```

---

### Get Items by Category

```
GET /api/v1/inventory-items/category?category=Electronics
```

---

### Get Low Stock Items

```
GET /api/v1/inventory-items/low-stock?threshold=5
```

Returns all items where `quantity <= threshold`.

---

### Update Item

```
PUT /api/v1/inventory-items/{id}
```

Request body follows the same structure as Create Item.

---

### Delete Item

```
DELETE /api/v1/inventory-items/{id}
```

**Response:** `204 No Content`

---

### Validation Rules

| Field | Rule |
|---|---|
| `name` | Required, 2–100 characters |
| `sku` | Required, must be unique |
| `category` | Required |
| `quantity` | Required, must be `>= 0` |
| `price` | Required, must be `> 0` |
| `supplierName` | Required |

---

## Error Responses

**404 Not Found**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Inventory item not found with id: 1"
}
```

**400 Validation Error**
```json
{
  "status": 400,
  "error": "Validation Failed",
  "messages": {
    "name": "Name is required"
  }
}
```

**409 Duplicate SKU**
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Inventory item with SKU already exists"
}
```

---

## CI/CD

GitHub Actions runs on every push and pull request to `master`:

1. Checks out the code
2. Sets up JDK 17 (Temurin)
3. Caches Maven dependencies
4. Runs `mvn clean verify`

Workflow file: `.github/workflows/ci.yml`

---

*Quantix Inventory Management API — Built with Spring Boot & Java 17*
