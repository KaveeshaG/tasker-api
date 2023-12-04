# Tasker!. API Setup

Task management app includes creating change status delete functionalities. This is an API of the Tasker!

Tasker! is a project that includes the following microservices: service-registry, API-gateway, config-server, and task-service.


## Prerequisites
### MySQL Server
Make sure you have MySQL Server installed. The project root directory includes the dump structure and data SQL file.

Setup your database connection inside the tasker-service application.yaml

Ex:
```yaml
datasource:
  url: "jdbc:mysql://localhost:3306/tasker"
  username: root
  password: password
  driver-class-name: com.mysql.cj.jdbc.Driver
```

## Zipkin
Run Zipkin using Docker:

```bash
docker run -d -p 9411:9411 openzipkin/zipkin
```
Zipkin is used for tracing API calls.

## Redis
Run Redis using Docker:
```bash
docker run --name latestredis -d -p6379:6379 redis
```
Redis is used for rate limiting to prevent DOS attacks.


## Project Startup

Follow the steps below to start the Tasker! project:

### 1. Service Registry
Start the `service-registry` microservice.

### 2. Config Server
Start the `config-server` microservice.

### 3. Tasker Service
Start the `task-service` microservice.

### 4. API-Gateway
Start the `cloud-gateway` microservice.

## Postman Collection
Access the Postman collection for testing the Tasker! APIs:

Tasker! [Postman Collection](https://shorturl.at/zEGL1)


## Note
Ensure that all dependencies, including Docker and required images, are available in your environment. The Docker commands will automatically pull the images if not present.