# HamaraShops User Service (`user-service`)

Production-ready User Management Microservice for the HamaraShops Platform, built with Java 21, Spring Boot 4.1.0, Spring Security 6, JWT, Spring Data JPA, and MySQL.

---

## 📌 Features

- **User Management APIs**: `POST /users`, `GET /users`, `GET /users/{id}`, `PUT /users/{id}`, `DELETE /users/{id}`
- **Security**: Spring Security 6, Stateless JWT Access Token authorization
- **Database**: MySQL `userdb` with JPA / Hibernate (`users`)
- **Containerization**: Multi-stage Dockerfile optimized for Google Cloud Run deployment

---

## 🚀 Running & Testing Standalone

```bash
cd D:\HamaraShops-Microservices\user-service
mvn clean package
java -jar target/user-service-0.0.1-SNAPSHOT.jar
```
