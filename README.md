# Task Manager REST API 


(Test Project So passwords and config in code for understanding the flow) - In production, secrets should be supplied through secure configuration or a secrets manager.

A backend RESTful API built using **Java and Spring Boot** to manage tasks securely. This project demonstrates REST API development, database persistence, authentication, authorization, CSRF protection, and integration testing.

## Tech Stack

- **Java**
- **Spring Boot 4**
- **Spring Web MVC**
- **Spring Data JPA / Hibernate**
- **H2 Database**
- **Spring Security**
- **JUnit 5 and MockMvc**
- **Maven**

## Key Features

- **RESTful API:** Developed REST controllers to expose endpoints for managing tasks.
- **CRUD Operations:** Create, retrieve, update, and delete tasks through HTTP endpoints.
- **Basic Authentication:** Secured API access using HTTP Basic Authentication.
- **Authorization:** Configured Spring Security to control access to protected endpoints.
- **CSRF Protection:** Explored CSRF token handling for state-changing HTTP requests.
- **Database Integration:** Used Spring Data JPA repositories to persist and retrieve tasks.
- **Layered Architecture:** Organized application logic into Controller, Service, Repository, and Entity layers.
- **Integration Testing:** Implemented API tests using JUnit 5 and Spring MockMvc.

## API Endpoints

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/tasks` | Retrieve all tasks |
| GET | `/tasks/{id}` | Retrieve a task by ID |
| POST | `/tasks` | Create a task |
| PUT | `/tasks/{id}` | Update an existing task |
| DELETE | `/tasks/{id}` | Delete a task |

## Security

Implemented Spring Security concepts including HTTP Basic Authentication, endpoint authorization, and CSRF protection.

The project demonstrates how authentication verifies user identity, authorization controls access to protected resources, and CSRF tokens protect applicable state-changing requests.

## Purpose

Built as a hands-on project to strengthen Java backend development skills, understand Spring Boot architecture, practice REST API security, and gain experience with integration testing.
