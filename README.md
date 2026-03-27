# Department Service
## Hospital Management System - Microservices Assignment
### IT4020 - Modern Topics in IT | SLIIT 2026

---

## Developer
- **Service:** Department Service
- **Port:** 8084

---

## Tech Stack
- Java 17
- Spring Boot 3.2.0
- PostgreSQL (Neon.tech)
- Spring Data JPA
- SpringDoc OpenAPI (Swagger)
- Maven

---

## Setup Instructions

### Step 1 - Clone the repository
```bash
git clone https://github.com/YOUR_ORG/department-service.git
cd department-service
```

### Step 2 - Configure Database
Open `src/main/resources/application.properties` and replace:
```properties
spring.datasource.url=YOUR_NEON_URL?sslmode=require
spring.datasource.username=YOUR_NEON_USERNAME
spring.datasource.password=YOUR_NEON_PASSWORD
```

### Step 3 - Run the service
```bash
mvn spring-boot:run
```

---

## API Endpoints

### Department CRUD
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /departments | Create department |
| GET | /departments | Get all departments |
| GET | /departments/{id} | Get department by ID |
| PUT | /departments/{id} | Update department |
| DELETE | /departments/{id} | Delete department |
| GET | /departments/search?name= | Search by name |
| GET | /departments/active | Get active departments |

### Doctor Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /departments/{id}/doctors | Add doctor to department |
| GET | /departments/{id}/doctors | Get doctors in department |
| DELETE | /departments/{id}/doctors/{doctorId} | Remove doctor |
| PUT | /departments/{id}/doctors/{doctorId}/set-head | Set head doctor |

---

## Swagger UI
- Direct: http://localhost:8084/swagger-ui.html
- Via Gateway: http://localhost:8080/departments/swagger-ui.html

---

## Database Tables
- `departments` - Stores department information
- `department_doctors` - Stores doctors assigned to departments
