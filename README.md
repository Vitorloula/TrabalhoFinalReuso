# Inventory Management System - Backend

This repository contains the **backend** of an Inventory Management System built with **Spring Boot**. It exposes a RESTful API to handle business logic and data operations such as product tracking, supplier management, order processing, and manual stock movements.

The backend is designed to serve as the core logic and data provider for the system. It can be consumed by any frontend or external system. A **frontend** client for this system is also available and can be found [here](https://github.com/Sebaspallero/inventory-managment-front).

## 🚀 Features

- **User Management**: Create and manage system users.
- **Product Management**: Register products with price and stock tracking.
- **Supplier Management**: Store and retrieve supplier data.
- **Order Management**: Create, view, update, and delete purchase orders linked to suppliers.
- **Order Items**: Each order includes one or more items, connected to products.
- **Inventory Movements**: Manually register `IN` (incoming) or `OUT` (outgoing) stock operations.
- **Stock Updates**: Stock levels are automatically updated when a movement is created.
- **Date-based Queries**: Retrieve orders or movements between date ranges.
- **User Authentication**: Register users with different roles: `EMPLOYEE` or `ADMIN`.
- **User Authorization**: Protect endpoints based on user role.
- **Export Data**: Export data from the system in PDF and Excel format.
- **Event Listener**: Record activities such as adding a product, creating an order, deleting a supplier, etc.

## 🧠 Tech Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- MySQL (or any SQL database)
- Spring Security + JWT
- MapStruct
- Maven
- Lombok
- Validation API
- JUnit & Mockito
- Postman for testing
- OpenApi for testing and visual endpoints.

## 📦 Entities Overview

### Product
- Basic unit with name, price, description, stock, etc.
- Connected to OrderItems and InventoryMovements.

### User
- Represents a system user performing inventory actions.

### Supplier
- Provides products.
- Linked to orders.

### Order & OrderItem
- Each order is placed to a supplier and contains multiple order items.

### InventoryMovement
- Manual stock movements (`IN`, `OUT`).
- Updates product stock on creation.
- Linked to both Product and User.

## 🧪 Sample Endpoints

- `POST /api/orders` – Create a new order
- `GET /api/orders/{id}` – Retrieve order by ID
- `POST /api/movements` – Create a manual inventory movement
- `GET /api/products` – List all products
- `GET /api/movements/product/{productId}` – Get movements by product

## ⚙️ Environment Configuration

Environment variables can be injected via `application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
```

Set variables in .vscode/launch.json or your environment before running the app.

## 🧰 How to Run

1. Clone the repo

2. Set up your database and configure environment variables

3. Build and run:

```bash
./mvnw spring-boot:run
```

## 🔗Related Repositories

- [Frontend Client - React App](https://github.com/Sebaspallero/inventory-managment-front).



## 📫 Contact

Feel free to reach out:

- GitHub: [@Sebaspallero](https://github.com/Sebaspallero) 
- Email: sebastianpallerodev@gmail.com

⭐ **Please consider giving it a star!** It helps visibility and motivates further development.