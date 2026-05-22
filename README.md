# Taskflow

A full-stack task and team management application built with a React + Vite frontend and a Spring Boot backend.

## Overview

Taskflow includes:

- A client application in `client/` using React 19, TypeScript, Vite, Tailwind CSS, TanStack Router, and React Query.
- A backend API in `server/` using Spring Boot 4, Java 17, Spring Data JPA, Spring Security, JWT authentication, and MySQL persistence.

The project supports user authentication plus REST APIs for teams, projects, tasks, and users.

## Key features

- JWT-based authentication and registration
- REST API with Spring MVC controllers
- MySQL-backed persistence using Spring Data JPA
- Task and project CRUD operations
- React SPA routing with TanStack Router
- Form handling with React Hook Form and validation via Zod
- Responsive UI using Tailwind CSS and Radix components

## Repository structure

- `client/` — frontend application
  - `src/` — application source
  - `src/routes/` — route definitions and page components
  - `src/components/` — reusable UI components and auth forms
  - `src/hooks/` — custom React hooks
  - `src/api/` — API utilities and client services

- `server/` — backend application
  - `src/main/java/com/taskflow/taskflow/` — Spring Boot application code
  - `src/main/java/com/taskflow/taskflow/rest/` — REST controllers
  - `src/main/java/com/taskflow/taskflow/service/` — service layer
  - `src/main/java/com/taskflow/taskflow/security/` — JWT and security config
  - `src/main/resources/application.properties` — application configuration

- `sql/` — database bootstrap and schema scripts

## Getting started

### Prerequisites

- Node.js/npm
- Java 17
- Maven (the project includes the Maven wrapper `./mvnw`)
- MySQL database

### Backend setup

1. Navigate to the backend folder:

   ```bash
   cd server
   ```

2. Configure database settings in `src/main/resources/application.properties`.
   Default values are:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/taskflow
   spring.datasource.username=root
   spring.datasource.password=
   ```

3. Create a MySQL database named `taskflow` or update the JDBC URL and credentials.

4. Run the backend:

   ```bash
   ./mvnw spring-boot:run
   ```

5. Run tests:
   ```bash
   ./mvnw test
   ```

### Frontend setup

1. Navigate to the frontend folder:

   ```bash
   cd client
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Start the frontend dev server:

   ```bash
   npm run dev
   ```

4. Run frontend tests:
   ```bash
   npm run test
   ```

## API highlights

The backend exposes routes under `/api`.

### Authentication

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/logout`

### Projects

- `GET /api/teams/{teamId}/projects`
- `POST /api/teams/{teamId}/projects`
- `GET /api/projects/{projectId}`
- `PATCH /api/projects/{projectId}`
- `DELETE /api/projects/{projectId}`

### Tasks

- `GET /api/projects/{projectId}/tasks`
- `POST /api/projects/{projectId}/tasks`
- `GET /api/tasks/{taskId}`

## Notes

- The backend uses JWT for stateless authentication. The client is expected to store and send tokens for protected requests.
- Database initialization scripts are available in `sql/`.
- Frontend routing is file-based under `client/src/routes/`.

## Development tips

- Start the backend first, then the frontend.
- Update the backend JDBC configuration before launching if your MySQL credentials differ.
- Use the existing tests in `client/src/` and `server/src/test/java/` as templates for new features.
