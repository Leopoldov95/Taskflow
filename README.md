# Taskflow

A full-stack task and team management application with a React + Vite frontend and a Spring Boot backend.

## Overview

Taskflow includes:

- A backend API in `server/` using Spring Boot 4, Java 17, Spring Data JPA, Spring Security, JWT authentication, Flyway migrations, and MySQL persistence.
- A frontend app in `client/` using React 19, TypeScript, Vite, Tailwind CSS, TanStack Router, and React Query.

The application supports authentication plus REST APIs for teams, projects, tasks, and users.

## Key features

- JWT-based authentication and authorization
- Spring Boot REST API with controller/service/repository layers
- MySQL persistence with Flyway database migrations
- Docker Compose infrastructure for backend and database
- React SPA with protected routes and API integration
- Responsive UI with Tailwind CSS and reusable components

## Repository structure

- `server/` — backend application and infrastructure
  - `Dockerfile` — multi-stage build for production container images
  - `docker-compose.yaml` — production-ready service definitions for app + MySQL
  - `docker-compose.override.yaml` — local build override and port mapping
  - `.env` — environment variables for Docker and Spring configuration
  - `src/main/java/com/taskflow/taskflow/` — Spring Boot application code
  - `src/main/java/com/taskflow/taskflow/rest/` — REST controllers
  - `src/main/java/com/taskflow/taskflow/service/` — service layer
  - `src/main/java/com/taskflow/taskflow/security/` — JWT and security config
  - `src/main/resources/application-prod.properties` — production profile config
  - `src/main/resources/db/migration/` — Flyway migration scripts

- `client/` — frontend application
  - `src/` — application source
  - `src/routes/` — route definitions and page components
  - `src/components/` — reusable UI and auth components
  - `src/hooks/` — custom React hooks
  - `src/api/` — API utilities and client services

- `sql/` — database bootstrap and schema scripts for local reference

## Backend and infrastructure

The backend is designed to run locally or inside Docker.

### Docker Compose

The backend includes `server/docker-compose.yaml` with two services:

- `mysql`
  - uses `mysql:8.0`
  - stores data in a Docker volume `mysql_data`
  - configured with `MYSQL_ROOT_PASSWORD`, `MYSQL_USER`, `MYSQL_PASSWORD`, and `DB_NAME`
- `app`
  - runs the Spring Boot jar on port `8080`
  - uses Spring profile `prod`
  - connects to the MySQL container using `jdbc:mysql://mysql:3306/${DB_NAME}`
  - depends on MySQL health check before starting

The override file `server/docker-compose.override.yaml` builds the app image from the local `server/Dockerfile` and maps the MySQL port to `3308` for local access.

### Environment configuration

The server expects a `.env` file in `server/` with values such as:

```env
SECURITY_JWT_SECRET=...
SECURITY_JWT_EXPIRATION=86400000
MYSQL_ROOT_PASSWORD=...
MYSQL_USER=...
MYSQL_PASSWORD=...
DB_NAME=taskflow
```

Spring configuration is driven by `server/src/main/resources/application-prod.properties`, which reads:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SECURITY_JWT_SECRET`
- `SECURITY_JWT_EXPIRATION`

The production profile enables Flyway migrations from `classpath:db/migration` and validates the JPA schema on startup.

### Server Dockerfile

The backend Dockerfile is a multi-stage build:

1. Build stage with `maven:3.9-eclipse-temurin-17` to compile and package the application.
2. Runtime stage with `eclipse-temurin:17-jre-alpine` to run the resulting `app.jar`.

## Running the backend

### With Docker Compose

From the repository root:

```bash
cd server
docker compose up --build
```

This starts both the MySQL database and the Spring Boot backend with the `prod` profile.

### Locally without Docker

1. Ensure Java 17 and Maven are installed.
2. Create a MySQL database named `taskflow` or update `server/src/main/resources/application.properties`.
3. Run the backend:

```bash
cd server
./mvnw spring-boot:run
```

4. Run backend tests:

```bash
./mvnw test
```

## Frontend setup

The frontend is independent from the backend and runs in `client/`.

1. Install dependencies:

```bash
cd client
npm install
```

2. Start the development server:

```bash
npm run dev
```

3. Run frontend tests:

```bash
npm run test
```

The client communicates with the backend API endpoints under `/api/v1` and uses JWT tokens for authenticated requests.

## API highlights

The backend exposes REST endpoints for authentication, teams, projects, and tasks.

API documentation is available via Swagger UI at: https://taskflow.leoortega.com/swagger-ui/index.html

### Authentication

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/logout`

### Teams

- `GET /api/v1/teams`
- `GET /api/v1/teams/{teamId}`
- `POST /api/v1/teams`
- `PATCH /api/v1/teams/{teamId}`
- `DELETE /api/v1/teams/{teamId}`
- `GET /api/v1/teams/{teamId}/members`
- `POST /api/v1/teams/{teamId}/members`
- `DELETE /api/v1/teams/{teamId}/members`

### Projects

- `GET /api/v1/teams/{teamId}/projects`
- `POST /api/v1/teams/{teamId}/projects`
- `GET /api/v1/projects/{projectId}`
- `PATCH /api/v1/projects/{projectId}`
- `DELETE /api/v1/projects/{projectId}`

### Tasks

- `GET /api/v1/projects/{projectId}/tasks`
- `POST /api/v1/projects/{projectId}/tasks`
- `GET /api/v1/tasks/{taskId}`

## Notes

- The backend is the main source of truth for authentication, authorization, and data persistence.
- Database migrations are managed by Flyway via `server/src/main/resources/db/migration/`.
- The client uses the API to render authenticated views and manage task/team state.
- Start the backend before the frontend when running locally.
