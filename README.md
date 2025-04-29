# Spring Boot PostgreSQL Demo

This is a demo Spring Boot application that demonstrates how to create a RESTful API with CRUD operations for a Student entity, connected to a PostgreSQL database running in Docker.

## Project Structure

- **Model**: Student entity with fields for id, firstName, lastName, email, and age
- **Repository**: JPA repository for database operations
- **Service**: Business logic layer
- **Controller**: REST API endpoints
- **Config**: Data initialization

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Gradle

## Running the Application

1. Start the PostgreSQL database:
   ```
   docker-compose up -d
   ```

2. Run the Spring Boot application:
   ```
   ./gradlew bootRun
   ```

3. The application will be available at http://localhost:8080

## API Endpoints

### Get all students
```
GET /api/students
```

### Get student by ID
```
GET /api/students/{id}
```

### Get students by last name
```
GET /api/students/by-lastname/{lastName}
```

### Get students by minimum age
```
GET /api/students/by-age/{age}
```

### Create a new student
```
POST /api/students
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "age": 20
}
```

### Update an existing student
```
PUT /api/students/{id}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "age": 21
}
```

### Delete a student
```
DELETE /api/students/{id}
```

## Sample Data

The application initializes with 5 sample students:
1. John Doe (20 years old)
2. Jane Smith (22 years old)
3. Bob Johnson (19 years old)
4. Alice Williams (21 years old)
5. Charlie Brown (23 years old)

## Testing with cURL

### Get all students
```
curl -X GET http://localhost:8080/api/students
```

### Create a new student
```
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Michael","lastName":"Scott","email":"michael.scott@example.com","age":35}'
```

### Update a student
```
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john.doe@example.com","age":21}'
```

### Delete a student
```
curl -X DELETE http://localhost:8080/api/students/1
```
