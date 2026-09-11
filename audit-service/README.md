# HamaraShops Audit Service (`audit-service`)

Production-ready Audit Log Management Microservice for the HamaraShops Platform, built with Java 21, Spring Boot 4.1.0, Spring Security 6, JWT, Spring Data JPA, and MySQL.

---

## 📌 Features

- **Audit Management APIs**: `POST /audit`, `GET /audit`, `GET /audit/{id}`, `DELETE /audit/{id}`
- **Security**: Spring Security 6, Stateless JWT Access Token authorization
- **Database**: MySQL `auditdb` with JPA / Hibernate (`audit_logs`)
- **Containerization**: Multi-stage Dockerfile optimized for Google Cloud Run deployment

---

## 🚀 Running & Testing Standalone

```bash
cd D:\HamaraShops-Microservices\audit-service
mvn clean package
java -jar target/audit-service-0.0.1-SNAPSHOT.jar
```
