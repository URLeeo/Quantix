# Quantix API

It's our project's Jira task manager------https://mammadzadaaslan05.atlassian.net/jira/software/projects/QUANTIX/boards/34?atlOrigin=eyJpIjoiMTE5NjUwNmQ1MGQ4NGE2MTgwOWY3MWU4OGQ2Njk4ZGEiLCJwIjoiaiJ9

Quantix is a Spring Boot REST API for inventory management.
It provides full CRUD operations for managing inventory items such as products, stock, categories, and suppliers.

--------------------------------------------------
PROJECT DESCRIPTION
--------------------------------------------------

Quantix is an inventory management system built with Spring Boot that supports:

- Inventory item CRUD operations
- Search by name or SKU
- Filter by category
- Low stock detection
- Pagination and sorting
- Validation handling
- Global exception handling

--------------------------------------------------
TECH STACK
--------------------------------------------------

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL / H2
- Lombok
- Maven

--------------------------------------------------
BASE URL
--------------------------------------------------

http://localhost:8080/api/v1/inventory-items---that will be change when we deploy the project

--------------------------------------------------
API ENDPOINTS
--------------------------------------------------

1. CREATE ITEM

POST /api/v1/inventory-items

Request Body:
{
  "name": "Laptop",
  "sku": "LP-1001",
  "category": "Electronics",
  "quantity": 10,
  "price": 1500,
  "supplierName": "Tech Supplier"
}

--------------------------------------------------

2. GET ITEM BY ID

GET /api/v1/inventory-items/{id}

Example:
GET /api/v1/inventory-items/1

--------------------------------------------------

3. GET ALL ITEMS

GET /api/v1/inventory-items

Supports:
- search (name or sku)
- category filter
- pagination
- sorting

Example:
GET /api/v1/inventory-items?page=0&size=10&sort=createdAt,desc

--------------------------------------------------

4. SEARCH + FILTER (same endpoint)

GET /api/v1/inventory-items?search=laptop
GET /api/v1/inventory-items?category=Electronics

--------------------------------------------------

5. GET ITEMS BY CATEGORY

GET /api/v1/inventory-items/category?category=Electronics

--------------------------------------------------

6. UPDATE ITEM

PUT /api/v1/inventory-items/{id}

--------------------------------------------------

7. DELETE ITEM

DELETE /api/v1/inventory-items/{id}

Response:
204 NO CONTENT

--------------------------------------------------

8. LOW STOCK ITEMS

GET /api/v1/inventory-items/low-stock?threshold=5

Returns items where quantity <= threshold

--------------------------------------------------
VALIDATION RULES
--------------------------------------------------

- name: required (2-100 chars)
- sku: required
- category: required
- quantity: must be >= 0
- price: must be > 0
- supplierName: required

--------------------------------------------------
ERROR RESPONSES
--------------------------------------------------

404 NOT FOUND
{
  "status": 404,
  "error": "Not Found",
  "message": "Inventory item not found with id: 1"
}

--------------------------------------------------

400 VALIDATION ERROR
{
  "status": 400,
  "error": "Validation Failed",
  "messages": {
    "name": "Name is required"
  }
}

--------------------------------------------------

409 DUPLICATE SKU
{
  "status": 409,
  "error": "Conflict",
  "message": "Inventory item with SKU already exists"
}

--------------------------------------------------
PAGINATION EXAMPLE
--------------------------------------------------

GET /api/v1/inventory-items?page=0&size=5&sort=createdAt,desc

--------------------------------------------------
SETUP INSTRUCTIONS
--------------------------------------------------

1. Clone project
git clone https://github.com/URLeeo/Quantix.git

2. Enter project
cd quantix

3. Build project
mvn clean install

4. Run project
mvn spring-boot:run

--------------------------------------------------
APPLICATION URL
--------------------------------------------------

http://localhost:8080

--------------------------------------------------
RUN TESTS
--------------------------------------------------

mvn test

--------------------------------------------------
PROJECT STRUCTURE
--------------------------------------------------

src/main/java/org/example/quantix

├── controller
├── service
├── repository
├── mapper
├── dto
│   ├── request
│   └── response
├── entity
├── exception

--------------------------------------------------
AUTHOR
--------------------------------------------------

Quantix Inventory Management API
Built with Spring Boot + Java 17
