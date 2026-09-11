# 🛍️ HamaraShops

### Enterprise Full-Stack Microservices Platform

HamaraShops is a production-grade full-stack platform built using **Java 21, Spring Boot, React 19, and Google Cloud Platform**. The system follows a scalable microservices architecture with independent domain services, API Gateway routing, JWT-based authentication, centralized audit logging, document management, and serverless cloud deployment.

The platform is designed to handle:

- User registration and authentication
- User profile management
- Candidate onboarding and management
- Candidate status evaluation
- Resume and document uploads
- Centralized audit logging
- Role-based access control
- Cloud-native deployment and autoscaling

---

## 🚀 Project Overview

HamaraShops was designed to overcome the limitations of traditional monolithic enterprise applications by separating business functionality into independent microservices.

The platform consists of:

- React 19 Single Page Application
- Spring Cloud API Gateway
- Authentication Service
- User Service
- Candidate Service
- Document Service
- Audit Service
- Google Cloud SQL
- Google Cloud Storage
- Google Cloud Run
- Google Artifact Registry

Each backend service contains its own domain logic, models, repositories, controllers, and exception handling.

The architecture supports both:

- **Local development using Netflix Eureka**
- **Production deployment using Google Cloud Run direct service routing**

---

# 🏗️ System Architecture

```text
                         ┌──────────────────────┐
                         │      React 19 SPA     │
                         │   Vite + Axios        │
                         └──────────┬───────────┘
                                    │
                                    │ REST / JSON
                                    ▼
                         ┌──────────────────────┐
                         │      API Gateway     │
                         │    Spring Gateway    │
                         │                      │
                         │  CORS                │
                         │  Routing             │
                         │  JWT Propagation     │
                         └──────────┬───────────┘
                                    │
              ┌─────────────────────┼─────────────────────┐
              │                     │                     │
              ▼                     ▼                     ▼
      ┌───────────────┐     ┌───────────────┐     ┌────────────────┐
      │ Auth Service  │     │ User Service  │     │Candidate Service│
      │    :8081      │     │    :8082      │     │     :8083       │
      └───────────────┘     └───────────────┘     └────────────────┘
              │                     │                     │
              └─────────────────────┼─────────────────────┘
                                    │
                         ┌──────────┴───────────┐
                         │                      │
                         ▼                      ▼
                 ┌───────────────┐      ┌────────────────┐
                 │Document Service│      │  Audit Service │
                 │     :8084      │      │      :8085     │
                 └───────┬───────┘      └────────────────┘
                         │
                         ▼
                 ┌─────────────────┐
                 │ Google Cloud    │
                 │ Storage (GCS)   │
                 └─────────────────┘

                         ┌─────────────────┐
                         │ Google Cloud SQL│
                         │ PostgreSQL/MySQL│
                         └─────────────────┘
```

---

# ✨ Key Features

## 🔐 Authentication & Authorization

- User registration
- Secure password hashing using BCrypt
- JWT-based authentication
- Access and refresh tokens
- Role-based authorization
- `ROLE_USER`
- `ROLE_ADMIN`
- Stateless authentication
- Automatic access-token refresh
- Protected frontend routes

### Token Configuration

| Token | Validity |
|---|---|
| Access Token | 15 minutes |
| Refresh Token | 7 days |

The React application automatically handles expired access tokens through an Axios refresh queue.

---

## 👤 User Management

The User Service manages:

- Full name
- Phone number
- Address
- User profile information
- User-related attributes

User operations are protected through Bearer Token authentication.

---

## 👨‍💼 Candidate Management

The Candidate Service manages the complete candidate application lifecycle.

### Candidate Status Flow

```text
APPLIED
   │
   ▼
UNDER_REVIEW
   │
   ├──────────────► ACCEPTED
   │
   └──────────────► REJECTED
```

Candidate information includes:

- First name
- Last name
- Email
- Qualification
- Experience level
- Skills
- Application status
- Resume URL
- Candidate metadata

Administrators can filter candidates based on:

- Status
- Qualification
- Experience level

---

## 📄 Document Management

The Document Service supports multi-part file uploads.

Supported file types include:

- PDF
- DOCX
- PNG

The service uses a pluggable storage strategy.

```text
                    Document Upload
                           │
                           ▼
                    Document Service
                           │
                           ▼
                    Storage Strategy
                           │
              ┌────────────┴────────────┐
              │                         │
              ▼                         ▼
       Local Storage                 GCS Storage
       (Development)                (Production)
```

### Cloud Storage

Production documents are stored in Google Cloud Storage.

**Google Cloud Storage Bucket:**

```text
hamarashops-documents-bucket
```

---

## 📝 Centralized Audit Logging

All important system operations are recorded through the Audit Service.

Audit records contain:

| Field | Description |
|---|---|
| `id` | Primary key |
| `userId` | User performing the action |
| `serviceName` | Source microservice |
| `action` | Executed action |
| `entityName` | Target business entity |
| `entityId` | Target record ID |
| `ipAddress` | Client IP address |
| `status` | SUCCESS / FAILURE |
| `createdAt` | Timestamp |

### Example Actions

```text
USER_REGISTERED
CANDIDATE_CREATED
CANDIDATE_UPDATED
DOCUMENT_UPLOADED
```

Audit logging is designed so that logging failures do not interrupt the main business operation.

---

# 🧩 Microservices

## 1. API Gateway

**Service:** `api-gateway`

### Responsibilities

- Single entry point for clients
- Request routing
- CORS handling
- JWT propagation
- Path rewriting
- Local/Cloud routing profiles

### Example Routes

```text
/api/auth/**
/api/users/**
/api/candidates/**
/api/documents/**
/api/audit/**
```

---

## 2. Auth Service

**Service:** `auth-service`

### Responsibilities

- User registration
- Login
- Password hashing
- JWT generation
- JWT validation
- Refresh token handling
- Role management
- Authentication

---

## 3. User Service

**Service:** `user-service`

### Responsibilities

- User profile management
- User demographic information
- Phone and address management
- User-related audit events

---

## 4. Candidate Service

**Service:** `candidate-service`

### Responsibilities

- Candidate registration
- Candidate application management
- Candidate status management
- Qualification management
- Skills
- Experience metadata
- Candidate filtering

---

## 5. Document Service

**Service:** `document-service`

### Responsibilities

- Multipart file uploads
- Resume management
- Document metadata
- Local file storage
- Google Cloud Storage integration

---

## 6. Audit Service

**Service:** `audit-service`

### Responsibilities

- Centralized audit logging
- Transaction tracking
- Service interaction tracking
- IP address tracking
- Success/failure tracking

---

# 🛠️ Technology Stack

## Backend

| Technology | Purpose |
|---|---|
| Java 21 | Backend programming language |
| Spring Boot 4.1.0 | Backend framework |
| Spring Cloud 2025.1.2 | Microservices infrastructure |
| Spring Cloud Gateway WebMVC | API Gateway |
| Spring Security | Security |
| JJWT 0.11.5 | JWT implementation |
| BCrypt | Password hashing |
| Spring Data JPA | Database persistence |
| RestTemplate | Inter-service communication |
| Netflix Eureka | Local service discovery |

---

## Frontend

| Technology | Purpose |
|---|---|
| React 19 | Frontend framework |
| Vite 6.1 | Frontend build tool |
| JavaScript ES6+ | Programming language |
| Tailwind CSS v4 | UI styling |
| Axios 1.7 | HTTP communication |
| React Router v7 | Client-side routing |
| Lucide Icons | UI icons |
| React Hot Toast | Notifications |

---

## Database & Storage

| Technology | Purpose |
|---|---|
| Google Cloud SQL | Relational database |
| PostgreSQL 15 | Production database |
| MySQL 8.0 | Supported database |
| HikariCP | Connection pooling |
| Google Cloud Storage | Document storage |

---

## Cloud & DevOps

| Technology | Purpose |
|---|---|
| Google Cloud Run | Serverless microservices |
| Google Artifact Registry | Container registry |
| Google Cloud Storage | File storage |
| Cloud SQL | Managed database |
| Docker | Containerization |
| Maven | Backend build |

---

# 🗄️ Database Architecture

HamaraShops follows the **Database-per-Service** pattern.

```text
                     Google Cloud SQL
                           │
          ┌────────────────┼────────────────────┐
          │                │                    │
          ▼                ▼                    ▼
       auth_db          user_db            candidate_db
          │                │                    │
          │                │                    │
     credentials        profiles           applications
     roles              addresses          status
     refresh tokens                         metadata


          ┌────────────────┴────────────────────┐
          │                                     │
          ▼                                     ▼
    document_db                              audit_db
          │                                     │
   document metadata                      audit transactions
   GCS references                          system actions
```

## Main Entities

```text
users
roles
refresh_tokens
candidates
documents
audit_logs
```

## Important Relationships

```text
users
  │
  └── role_id ──────► roles.id


refresh_tokens
  │
  └── user_id ──────► users.id


candidates
  │
  └── user_id ──────► users.id


documents
  │
  └── candidate_id ─► candidates.id


audit_logs
  │
  └── user_id ──────► users.id
```

---

# 🔌 REST API

All APIs are exposed through the API Gateway.

## Authentication

### Register

```http
POST /api/auth/register
```

**Access:** Public

### Login

```http
POST /api/auth/login
```

**Access:** Public

### Refresh Token

```http
POST /api/auth/refresh
```

**Access:** Public

---

## User

### Get User Profile

```http
GET /api/users/{id}
```

**Authentication:** Bearer Token Required

---

## Candidate

### Get Candidates

```http
GET /api/candidates
```

**Access:** `ROLE_ADMIN`

### Create Candidate

```http
POST /api/candidates
```

**Authentication:** Bearer Token Required

---

## Documents

### Upload Document

```http
POST /api/documents/upload
```

**Authentication:** Bearer Token Required

**Content Type:**

```text
multipart/form-data
```

---

## Audit

### Get Audit Logs

```http
GET /api/audit/logs
```

**Access:** `ROLE_ADMIN`

---

# 🔒 Security Architecture

HamaraShops implements security independently across the microservices.

Protected APIs require:

```http
Authorization: Bearer <JWT_TOKEN>
```

### Request Flow

```text
Client
   │
   ▼
API Gateway
   │
   ▼
JWT Authentication Filter
   │
   ├── Valid Token ──────► SecurityContext
   │
   └── Invalid Token ────► 401 Unauthorized
```

### Security Technologies

- Spring Security
- JWT
- BCrypt
- Role-based authorization
- HTTPS
- GCP IAM
- Cloud SQL secure connectivity
- GCS service account authentication

---

# 🔄 JWT Refresh Flow

The React frontend contains an automatic token refresh mechanism.

```text
API Request
     │
     ▼
Access Token
     │
     ▼
API Gateway
     │
     ▼
401 Unauthorized
     │
     ▼
Axios Interceptor
     │
     ▼
Refresh Token
     │
     ▼
/api/auth/refresh
     │
     ▼
New Access Token
     │
     ▼
Replay Queued Requests
```

This prevents users from being unnecessarily logged out when the access token expires.

---

# 🌐 Local Development Architecture

During local development, the system uses Netflix Eureka for service discovery.

```text
                    Eureka Server
                         :8761
                           │
           ┌───────────────┼───────────────┐
           │               │               │
           ▼               ▼               ▼
     Auth Service    User Service    Candidate Service
           │               │               │
           └───────────────┼───────────────┘
                           │
                      API Gateway
                           │
                      React Frontend
```

The local gateway uses service discovery such as:

```text
lb://AUTH-SERVICE
lb://USER-SERVICE
lb://CANDIDATE-SERVICE
```

---

# ☁️ Google Cloud Architecture

In production, HamaraShops runs on Google Cloud Platform.

```text
                          Internet
                             │
                             ▼
                       React Frontend
                             │
                             ▼
                        API Gateway
                         Cloud Run
                             │
             ┌───────────────┼───────────────────┐
             │               │                   │
             ▼               ▼                   ▼
       Auth Service     User Service      Candidate Service
       Cloud Run       Cloud Run          Cloud Run
             │               │                   │
             └───────────────┼───────────────────┘
                             │
                  ┌──────────┴──────────┐
                  │                     │
                  ▼                     ▼
           Google Cloud SQL       Document Service
             PostgreSQL              Cloud Run
                                        │
                                        ▼
                              Google Cloud Storage

                             │
                             ▼
                       Audit Service
                         Cloud Run
```

---

# 🚀 Google Cloud Deployment

The production deployment uses:

- Google Cloud Run
- Google Artifact Registry
- Google Cloud SQL
- Google Cloud Storage
- IAM
- Cloud SQL Auth Proxy

All six backend microservices are deployed independently:

```text
api-gateway
auth-service
user-service
candidate-service
document-service
audit-service
```

---

# 🐳 Docker

Every backend microservice uses a multi-stage Docker build.

## Build Stage

```dockerfile
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests
```

## Production Stage

```dockerfile
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENV PORT=8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

The multi-stage build keeps Maven and build tools out of the final production image.

---

# 📦 Artifact Registry

Docker images are stored in Google Artifact Registry.

### Authentication

```bash
gcloud auth configure-docker asia-south1-docker.pkg.dev
```

### Build Image

```bash
docker build \
  -t asia-south1-docker.pkg.dev/hamarashops-cloud/hamarashops-repo/api-gateway:v1.0.0 .
```

### Push Image

```bash
docker push \
  asia-south1-docker.pkg.dev/hamarashops-cloud/hamarashops-repo/api-gateway:v1.0.0
```

---

# ☁️ Cloud Run Deployment

Example:

```bash
gcloud run deploy api-gateway \
  --image=asia-south1-docker.pkg.dev/hamarashops-cloud/hamarashops-repo/api-gateway:v1.0.0
```

Each microservice runs as an independent Cloud Run service.

## Cloud Run Configuration

| Service | CPU | Memory | Scale |
|---|---:|---:|---:|
| API Gateway | 1 vCPU | 512 MB | 1–10 |
| Auth Service | 1 vCPU | 1 GB | 0–5 |
| User Service | 1 vCPU | 512 MB | 0–5 |
| Candidate Service | 1 vCPU | 1 GB | 0–5 |
| Document Service | 1 vCPU | 1 GB | 0–5 |
| Audit Service | 1 vCPU | 512 MB | 0–5 |

Cloud Run provides:

- Automatic scaling
- Scale-to-zero
- HTTPS
- SSL termination
- Container lifecycle management
- Horizontal scaling

---

# 🗃️ Cloud SQL

Production relational persistence uses Google Cloud SQL.

The documented production configuration includes:

```text
Database Engine: PostgreSQL 15
Storage: SSD 50 GB
Storage Expansion: Automatic
Connection: Cloud SQL Auth Proxy / Unix Socket
```

Logical service databases include:

```text
auth_db
user_db
candidate_db
document_db
audit_db
```

---

# ⚡ HikariCP Configuration

The services use HikariCP for database connection pooling.

Example configuration:

```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.connection-timeout=20000
```

Production database credentials should be supplied through environment variables.

```properties
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

---

# 🌐 Cloud Routing vs Local Routing

HamaraShops uses Spring Profiles to support both environments.

## Local

```properties
SPRING_PROFILES_ACTIVE=default
```

- Eureka enabled
- Dynamic service discovery
- `lb://SERVICE-NAME`
- Eureka Server on port `8761`

## Production

```properties
SPRING_PROFILES_ACTIVE=cloud
```

- Eureka disabled
- Direct Cloud Run URLs
- Environment-based service configuration
- Native GCP HTTPS routing

### Local

```text
API Gateway
     │
     ▼
  Eureka
     │
     ▼
  Services
```

### Production

```text
API Gateway
     │
     ▼
Cloud Run Service URLs
```

This avoids unnecessary Eureka overhead in the serverless production environment.

---

# 🖥️ Frontend Architecture

The React frontend follows a modular architecture.

```text
src/
│
├── components/
│   ├── CandidateDashboard.jsx
│   ├── Login.jsx
│   ├── Register.jsx
│   └── ...
│
├── pages/
│   ├── APIDocsPage.jsx
│   ├── ServicePage.jsx
│   ├── AboutPage.jsx
│   └── ...
│
├── services/
│   ├── axiosInstance.js
│   ├── authService.js
│   ├── candidateService.js
│   └── ...
│
├── App.jsx
└── main.jsx
```

## Frontend Responsibilities

- Authentication UI
- Registration
- Login
- Candidate dashboard
- Candidate management
- Protected routing
- API communication
- JWT handling
- Token refresh
- Toast notifications
- Responsive UI

---

# 📁 Backend Project Structure

```text
HamaraShops-Microservices/
│
├── api-gateway/
│   ├── Gateway Configuration
│   ├── CORS Configuration
│   └── Cloud Gateway Configuration
│
├── auth-service/
│   ├── AuthController
│   ├── JwtTokenProvider
│   └── SecurityConfig
│
├── user-service/
│   ├── UserController
│   ├── UserServiceImpl
│   └── User Model
│
├── candidate-service/
│   ├── CandidateController
│   └── Candidate Model
│
├── document-service/
│   ├── DocumentController
│   └── GcsStorageServiceImpl
│
├── audit-service/
│   ├── AuditLogController
│   └── AuditLog Model
│
└── eureka-cloud-server/
    └── Eureka Discovery Server
```

---

# 🧪 System Requirements

## Development Environment

| Requirement | Specification |
|---|---|
| OS | Windows 11 64-bit / Ubuntu 22.04 |
| CPU | Quad-core Intel Core i7 / AMD Ryzen 7 |
| RAM | 16 GB minimum |
| Recommended RAM | 32 GB |
| Storage | 500 GB NVMe SSD |
| Java | OpenJDK 21 |
| Node.js | v24.16 |
| npm | 11.13 |

---

# 🔧 Local Setup

## 1. Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd HamaraShops-Microservices
```

## 2. Start Eureka Server

```bash
cd eureka-cloud-server
mvn spring-boot:run
```

Eureka Server:

```text
http://localhost:8761
```

---

## 3. Start Backend Services

Start each service separately.

### API Gateway

```bash
cd api-gateway
mvn spring-boot:run
```

### Auth Service

```bash
cd auth-service
mvn spring-boot:run
```

### User Service

```bash
cd user-service
mvn spring-boot:run
```

### Candidate Service

```bash
cd candidate-service
mvn spring-boot:run
```

### Document Service

```bash
cd document-service
mvn spring-boot:run
```

### Audit Service

```bash
cd audit-service
mvn spring-boot:run
```

---

# 🎨 Frontend Setup

Navigate to the frontend project:

```bash
cd HamaraShops-Frontend
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

The frontend runs using Vite.

```text
http://localhost:5173
```

---

# 🔐 Environment Configuration

Sensitive credentials should **never be committed to GitHub**.

Example environment variables:

```env
DB_NAME=
DB_USER=
DB_PASSWORD=

JWT_SECRET=

AUTH_SERVICE_URL=
USER_SERVICE_URL=
CANDIDATE_SERVICE_URL=
DOCUMENT_SERVICE_URL=
AUDIT_SERVICE_URL=

GCP_BUCKET_NAME=
GOOGLE_APPLICATION_CREDENTIALS=
```

For production deployments, configure secrets through the appropriate Google Cloud security mechanisms rather than hardcoding credentials.

---

# 🛡️ Exception Handling

Every microservice implements global exception handling using:

```java
@RestControllerAdvice
```

Standard error responses contain information such as:

```json
{
  "timestamp": "2026-08-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found",
  "path": "/api/candidates/10"
}
```

This provides consistent error responses across the microservices.

---

# 🔄 Inter-Service Communication

Services communicate using HTTP/REST.

### Example Audit Flow

```text
Auth Service
     │
     │ REST POST
     ▼
AuditServiceClient
     │
     │ /api/audit/logs
     ▼
Audit Service
```

The `AuditServiceClient` uses `RestTemplate` to send audit events.

Audit failures are handled without breaking the main business transaction.

---

# ⚙️ Engineering Challenges & Solutions

## Challenge 1 — CORS Pre-Flight Failures

### Problem

Cross-origin requests and authorization headers caused CORS pre-flight issues.

### Solution

Centralized CORS configuration at the API Gateway.

---

## Challenge 2 — JWT Token Refresh Race Conditions

### Problem

Multiple API requests could receive `401 Unauthorized` simultaneously when an access token expired.

### Solution

Implemented a subscriber queue in:

```text
axiosInstance.js
```

Requests wait for the refresh operation and are replayed after a new token is received.

---

## Challenge 3 — Eureka in Serverless Cloud

### Problem

Eureka-based service discovery is unnecessary in Google Cloud Run.

### Solution

Implemented Spring Profile-based routing.

```text
Local  → Eureka Service Discovery
Cloud  → Direct Cloud Run URLs
```

---

# 📈 Scalability

The microservices architecture allows each service to scale independently.

For example:

```text
High Candidate Traffic
        │
        ▼
Candidate Service
        │
        ├── Instance 1
        ├── Instance 2
        ├── Instance 3
        └── Instance N
```

Other services do not need to scale unnecessarily.

Google Cloud Run provides automatic horizontal scaling based on traffic.

---

# 💡 Advantages

### Independent Services

Each business domain can be developed and deployed independently.

### Fault Isolation

Failure in one service is less likely to bring down the entire application.

### Independent Scaling

Individual services can scale according to their own traffic requirements.

### Stateless Authentication

JWT allows services to validate authentication without maintaining server-side sessions.

### Cloud Native

The platform is designed for Google Cloud Run and managed cloud infrastructure.

### Secure Storage

Documents are stored in Google Cloud Storage with service-account-based access.

### Centralized Auditing

System operations are captured through a dedicated audit service.

### Environment Flexibility

The same architecture supports local Eureka-based development and Cloud Run production routing.

---

# 🔮 Future Enhancements

The documented roadmap includes:

- Google Cloud Pub/Sub for asynchronous event streaming
- Redis caching
- Terraform Infrastructure as Code
- Further cloud-native optimization
- Expanded event-driven communication

### Future Architecture

```text
Microservices
     │
     ▼
Google Cloud Pub/Sub
     │
     ├── Audit Events
     ├── Notifications
     ├── Candidate Events
     └── Document Events
```

---

# 📊 Production Architecture Summary

| Component | Technology |
|---|---|
| Frontend | React 19 + Vite |
| API Gateway | Spring Cloud Gateway WebMVC |
| Backend | Java 21 + Spring Boot 4.1 |
| Authentication | Spring Security + JWT |
| Password Security | BCrypt |
| Service Discovery | Eureka (Local) |
| Cloud Routing | Cloud Run Direct URLs |
| Database | Cloud SQL |
| ORM | Spring Data JPA |
| Connection Pool | HikariCP |
| File Storage | Google Cloud Storage |
| Containers | Docker |
| Container Registry | Artifact Registry |
| Deployment | Google Cloud Run |
| Audit Logging | Dedicated Audit Service |

---

# 🏆 Project Status

**Status:** Fully Deployed & Verified

**Version:** `v1.0.0`

**Review:** Production Review

**Cloud:** Google Cloud Platform

The technical documentation describes the platform as meeting its functional, architectural, security, and cloud deployment objectives, with a decoupled architecture designed for resilience and serverless autoscaling.

---

# 👨‍💻 Author

**Ravi — Senior Full Stack Engineer**

---

# 📚 Documentation

For detailed architecture information, including:

- System architecture
- Microservices responsibilities
- Database ERD
- API Gateway routing
- JWT authentication workflow
- Candidate management
- Document storage
- Audit logging
- Docker architecture
- Cloud SQL
- Cloud Run
- Google Cloud Storage
- IAM security
- Deployment configuration

refer to the project's technical documentation.

---

# ⭐ Key Takeaway

HamaraShops demonstrates a complete enterprise-style microservices implementation using **Java Spring Boot, React, Docker, and Google Cloud Platform**.

The project combines:

```text
Modern Frontend
       +
Microservices
       +
API Gateway
       +
JWT Security
       +
Database-per-Service
       +
Cloud Storage
       +
Docker
       +
Google Cloud Run
       +
Centralized Auditing
```

to create a **scalable, secure, and cloud-ready application architecture**.
