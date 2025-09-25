# Spring Boot Microservices Project – Product, Inventory & Order System

This project is a **backend-focused microservices application** built with **Spring Boot**, demonstrating a **secure, scalable, and event-driven architecture**. The system is designed for practicing **distributed systems, service discovery, token-based authentication, asynchronous messaging, and monitoring**.

## 📌 Project Overview

The application consists of **three core microservices**:

- **Product Service**  
  Stores product details: `productId`, `name`, `price`, `expiryDate`, and `forSale` (1 = available, 0 = not for sale).  

- **Inventory Service**  
  Maintains stock levels: `itemId`, `productId`, `category`, `quantity`.  

- **Order Service**  
  Handles customer orders and validates:
  - Product availability (`forSale` flag) from Product Service
  - Stock quantity from Inventory Service

### 🔑 Technologies Used

- **Spring Boot** – Each microservice is an independent Spring Boot application.
- **API Gateway** – Central entry point for all requests, handles routing, load balancing, and token validation.
- **Keycloak** – Authentication and authorization using **OAuth2 / JWT tokens**.
- **Netflix Eureka** – Service registration and discovery for dynamic inter-service communication.
- **Apache Kafka** – Event-driven communication between microservices (e.g., order notifications, inventory updates).
- **Prometheus & Grafana** – Metrics collection and visualization for monitoring microservice health.

---

## ⚡ Architecture Overview

```text
[API Client] --> [API Gateway] --> [Keycloak: Token Validation]
                                 --> [Product Service]
                                 --> [Inventory Service]
                                 --> [Order Service]
[Microservices] <--> [Eureka Server: Service Discovery]
[Microservices Metrics] --> [Prometheus] --> [Grafana Dashboard]
[Order / Inventory Events] --> [Kafka Topics]
