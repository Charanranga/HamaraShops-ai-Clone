# HamaraShops Candidate Service (`candidate-service`)

Production-ready Candidate Management Microservice for the HamaraShops Platform, built with Java 21, Spring Boot 4.1.0, Spring Security 6, JWT, Spring Data JPA, and MySQL.

---

## 📌 Features

- **Candidate Management APIs**: `POST /candidates`, `GET /candidates`, `GET /candidates/{id}`, `PUT /candidates/{id}`, `DELETE /candidates/{id}`
- **Security**: Spring Security 6, Stateless JWT Access Token authorization
- **Database**: MySQL `candidatedb` with JPA / Hibernate (`candidates`)
- **Containerization**: Multi-stage Dockerfile optimized for Google Cloud Run deployment

---

## 🚀 Running & Testing Standalone

```bash
cd D:\HamaraShops-Microservices\candidate-service
mvn clean package
java -jar target/candidate-service-0.0.1-SNAPSHOT.jar
```
