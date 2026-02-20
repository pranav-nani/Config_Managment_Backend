# 📘 Config Management System

A centralized configuration management service built using **Java, Spring Boot, and MongoDB** that allows dynamic storage, versioning, and retrieval of JSON/YAML configuration files for microservices.

---

## 🚀 Problem Statement

In traditional systems, configuration values are stored inside local JSON/YAML files within applications.
Whenever configuration changes are required:

* JSON file must be modified
* Application must be rebuilt
* Application must be redeployed

This leads to:

* Deployment overhead
* Lack of version control
* No rollback mechanism
* Environment mismanagement (dev/test/prod)
* Configuration duplication across services

---

## ✅ Solution

This project provides a **centralized configuration service** where:

* Admins upload configuration files (JSON/YAML)
* Configurations are stored in MongoDB
* Each update creates a new version
* Services fetch configurations dynamically at runtime
* No redeployment required for config changes
* Supports rollback and environment isolation

---

## 🏗️ Architecture Overview

```
                Admin (Upload Config)
                        │
                        ▼
             Config Management Service
                 (Spring Boot API)
                        │
                        ▼
                     MongoDB
                        ▲
                        │
         Microservices fetch configs dynamically
```

---

## 🧱 Tech Stack

* Java 17
* Spring Boot
* Spring Data MongoDB
* MongoDB Atlas
* Maven
* Lombok
* Spring Validation

---

## 📂 Project Structure

```
config-management-system/
│
├── controller/
│   └── ConfigController.java
│
├── service/
│   └── ConfigService.java
│
├── repository/
│   └── ServiceConfigRepository.java
│
├── model/
│   └── ServiceConfig.java
│
├── dto/
│   └── ConfigRequest.java
│
└── application.yml
```

---

## 🗄️ MongoDB Document Structure

Collection: `service_configs`

```json
{
  "_id": "ObjectId",
  "serviceName": "payment-service",
  "environment": "prod",
  "version": 2,
  "configData": {
    "timeout": 10000,
    "retry": 3,
    "baseUrl": "https://api.payment.com"
  },
  "isActive": true,
  "createdAt": "2026-02-18T10:20:00",
  "createdBy": "admin"
}
```

---

## 🔄 Core Features

### 1️⃣ Upload Configuration

* Accepts JSON/YAML
* Validates format
* Automatically increments version
* Stores metadata

### 2️⃣ Versioning

* Every upload creates a new version
* Old versions preserved
* Easy rollback

### 3️⃣ Environment Isolation

Supports:

* dev
* test
* prod

Example:

```
payment-service | dev  | v1
payment-service | prod | v3
```

### 4️⃣ Dynamic Retrieval

Microservices fetch configuration using REST APIs.

### 5️⃣ Rollback Support

Older versions can be reactivated if needed.

---

## 📡 REST API Endpoints

### Upload Config

```
POST /configs/upload
```

Request Body:

```json
{
  "serviceName": "payment-service",
  "environment": "prod",
  "configData": {
    "timeout": 10000,
    "retry": 3
  }
}
```

---

### Get Latest Config

```
GET /configs/{serviceName}/{environment}
```

Example:

```
GET /configs/payment-service/prod
```

---

### Get Specific Version

```
GET /configs/{serviceName}/{environment}?version=2
```

---

### Get Version History

```
GET /configs/history/{serviceName}/{environment}
```

---

### Delete Config (Optional)

```
DELETE /configs/{id}
```

---

## 🔁 Workflow Explanation

### Upload Flow

```
Admin
   ↓
POST /configs/upload
   ↓
Validate JSON
   ↓
Find latest version
   ↓
Increment version
   ↓
Store in MongoDB
```

---

### Fetch Flow

```
Microservice
   ↓
GET /configs/payment-service/prod
   ↓
Config Service
   ↓
MongoDB
   ↓
Return JSON config
```

No redeployment required for configuration changes.

---

## 🧠 How Microservices Use This

Instead of reading local JSON:

```json
application-config.json
```

Microservices call:

```
GET http://config-service/configs/payment-service/prod
```

And dynamically load configuration at runtime.


## 🧪 Running the Application

### 1️⃣ Clone Repository

```
git clone https://github.com/your-username/config-management-system.git
```

### 2️⃣ Configure MongoDB

Update `application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://username:password@cluster.mongodb.net/configdb
```

### 3️⃣ Run Application

```
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```


