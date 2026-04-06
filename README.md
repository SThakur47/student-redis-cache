# High-Performance Data Retrieval System Using Redis Caching

## Tech Stack
- Java 17
- Spring Boot 3.2
- Hibernate JPA
- PostgreSQL
- Redis

## Setup

### Prerequisites
- Java 17
- PostgreSQL running on port 5432
- Redis running on port 6379

### Configuration
Edit `src/main/resources/application.properties`:

spring.datasource.url=jdbc:postgresql://localhost:5432/studentdb

spring.datasource.username=postgres

spring.datasource.password=yourpassword

spring.data.redis.host=localhost

spring.data.redis.port=6379


### Run
```bash
mvn spring-boot:run
```

## API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/students | Create student |
| GET | /api/students | Get all students |
| GET | /api/students/{id} | Get by ID (Cached) |
| PUT | /api/students/{id} | Update student |
| DELETE | /api/students/{id} | Delete student |

## Cache Demo
- First GET /api/students/{id} → **Cache MISS** (hits PostgreSQL)
- Second GET /api/students/{id} → **Cache HIT** (served from Redis)
