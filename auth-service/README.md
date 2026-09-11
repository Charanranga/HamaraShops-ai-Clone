# HamaraShops Auth Service (`auth-service`)

Production-ready Authentication and User Management Microservice for the HamaraShops Platform, built with Java 21, Spring Boot 4.1.0, Spring Security 6, JWT, Spring Data JPA, and MySQL.

---

## 📌 Features

- **Authentication APIs**: `/auth/register`, `/auth/login`, `/auth/refresh`, `/auth/logout`
- **User Management APIs**: `/users/profile`, `/users/change-password`
- **Admin APIs**: `GET /users`, `GET /users/{id}`, `PUT /users/{id}`, `DELETE /users/{id}`
- **Security**: Spring Security 6, BCrypt password hashing, JWT Access & Refresh Token authorization
- **Database**: MySQL `authdb` with JPA / Hibernate (`users`, `roles`, `refresh_tokens`)
- **Containerization**: Multi-stage Dockerfile optimized for Google Cloud Run deployment

---

## 🚀 Running & Testing Standalone

```bash
cd D:\HamaraShops-Microservices\auth-service
mvn clean package
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```
