# HamaraShops Document Service (`document-service`)

Production-ready Document Management Microservice for the HamaraShops Platform, built with Java 21, Spring Boot 4.1.0, Spring Security 6, JWT, Spring Data JPA, and MySQL.

---

## 📌 Features

- **Document Management APIs**: `POST /documents`, `GET /documents`, `GET /documents/{id}`, `PUT /documents/{id}`, `DELETE /documents/{id}`
- **Security**: Spring Security 6, Stateless JWT Access Token authorization
- **Database**: MySQL `documentdb` with JPA / Hibernate (`documents`)
- **Containerization**: Multi-stage Dockerfile optimized for Google Cloud Run deployment

---

## 🚀 Running & Testing Standalone

```bash
cd D:\HamaraShops-Microservices\document-service
mvn clean package
java -jar target/document-service-0.0.1-SNAPSHOT.jar
```
