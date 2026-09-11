# HamaraShops API Gateway (`api-gateway`)

API Gateway Infrastructure Microservice for the HamaraShops Enterprise Architecture, built with Java 21, Spring Boot 4.1.0, and Spring Cloud 2025.1.2.

---

## 📌 Architecture Highlights

- **Maven Multi-Module Architecture**: Child module inheriting dependencies from `com.hamarashops:hamarashops-parent:0.0.1-SNAPSHOT`.
- **Spring Cloud Gateway Server WebMVC**: Uses `spring-cloud-starter-gateway-server-webmvc` matching the reference enterprise architecture.
- **Eureka Discovery Client**: Registers with Eureka Discovery Server (`http://localhost:8761/eureka/`).
- **Config Server Integration**: Direct configuration fetching from `http://localhost:8888`.
- **Cloud Run & Docker Ready**: Multi-stage Docker build with dependency layer caching and dynamic `${PORT}` binding.

---

## 🛠 Tech Stack

- **Java**: 21
- **Spring Boot**: 4.1.0
- **Spring Cloud**: 2025.1.2 (`spring-cloud-starter-gateway-server-webmvc`, `spring-cloud-starter-config`, `spring-cloud-starter-netflix-eureka-client`)
- **Port**: `8080` (default, dynamic `${PORT}`)

---

## 🚀 Running & Building

### 1. Build from Multi-Module Parent
```bash
cd D:\HamaraShops-Microservices\hamarashops-parent
mvn clean compile
```

### 2. Build & Test Standalone Microservice
```bash
cd D:\HamaraShops-Microservices\api-gateway
mvn clean package
```

### 3. Run Locally
```bash
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```
