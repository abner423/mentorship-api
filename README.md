# 🧠 Mentorship API

This is a **simple Java and Spring Boot API** created for study purposes. It simulates a mentorship system where students can request sessions with mentors. Despite being a learning project, it covers several important backend development concepts.

## ✅ Features

- Student and Mentor registration
- Session request and management
- Mentor approval or rejection of sessions
- Pagination and filtering of sessions
- Uses `LocalDateTime`, Enums, and DTOs for clean architecture
- Endpoints follow **RESTful conventions**

## 🛠️ Tech & Concepts Used

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Bean Validation (Jakarta Validation)**
- **Object-Oriented Programming**
- **Exception Handling**
- **Multi-threading with ExecutorService**
- **Custom error response**
- **Swagger (OpenAPI) for documentation**
- **Spring Security** (basic setup for extensibility)
- **Docker** (for local database testing)

## 📦 API Endpoints

### Student

- `POST /students` – Create a new student
- `GET /students` – Get all students (paginated)
- `POST /students/{studentId}/sessions` – Request a session with a mentor
- `GET /students/{studentId}/sessions` – List last 5 sessions
- `GET /students/{studentId}/sessions/next` – Get next upcoming session

### Mentor

- `POST /mentors` – Create a new mentor
- `GET /mentors` – List all mentors
- `GET /mentors/{mentorId}/sessions` – List all mentor sessions
- `GET /mentors/{mentorId}/sessions/pending` – List pending sessions
- `PATCH /mentors/{mentorId}/sessions/{sessionId}/approve` – Approve session
- `PATCH /mentors/{mentorId}/sessions/{sessionId}/cancel` – Reject session

## 🐳 Running with Docker (Database)

To spin up a local MySQL database using Docker, run:

```bash
docker build -t mentorship-db-image .
docker run -d -p 3306:3306 --name mentorship-db mentorship-db-image
