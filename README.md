# Task Tracker API - roadmap.sh

https://roadmap.sh/projects/task-tracker

Backend REST API developed in Java with Spring Boot for task tracking and task list management, based on the Task Tracker challenge from roadmap.sh.

---

## Tech Stack

* Java 17+
* Spring Boot 3+ (Spring Web, Spring Data JPA)
* PostgreSQL / H2 Database
* Lombok
* Maven

---

## Task Properties (TaskEntity)

Each stored task has the following data structure:

* `id` (`UUID`): Unique identifier for the task.
* `userId` (`UUID`): Identifier of the user who owns the task.
* `title` (`String`): Short title or summary of the task.
* `description` (`String`): Detailed description of the activity.
* `status` (`statusTaskEnum` / `String`): Task execution status (`PENDING`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`).
* `priority` (`String`): Priority level (e.g., `HIGH`, `MEDIUM`, `LOW`).
* `deadline` (`OffsetDateTime`): Target completion date and time.
* `overdueNotified` (`Boolean`): Flag indicating if an overdue notification was sent.
* `createdAt` (`OffsetDateTime`): Creation timestamp (automatically generated).
* `updatedAt` (`OffsetDateTime`): Last update timestamp (automatically generated).

---

## API Endpoints

The API exposes the following routes under the `/todo/api` prefix:

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/todo/api/createtask` | Registers a new task in the system. |
| `POST` | `/todo/api/updatetask/{id}` | Partially updates fields of an existing task. |
| `POST` | `/todo/api/taskdone/{id}` | Marks the status of a specific task as `COMPLETED`. |
| `POST` | `/todo/api/deletetask/{id}` | Deletes a task from the database by its ID. |
| `GET` | `/todo/api/listall` | Returns a list of all registered tasks. |
| `POST` / `GET` | `/todo/api/alldone` | Filters and returns only tasks with status `COMPLETED`. |
| `POST` / `GET` | `/todo/api/allpending` | Filters and returns only tasks with status `PENDING` / `IN_PROGRESS`. |
| `POST` / `GET` | `/todo/api/allcancelated` | Filters and returns only tasks with status `CANCELLED`. |
| `POST` | `/todo/api/user` | Creates a new user in the system. |

---

## Usage Examples

### 1. Create a Task (`POST /todo/api/createtask`)

**Request Body (JSON):**
```json
{
  "title": "Buy groceries",
  "description": "Buy milk, bread, and coffee at the supermarket",
  "status": "PENDING",
  "priority": "HIGH",
  "userId": "8ab0e206-e6e2-4815-8d92-7e53f2cbeed6",
  "deadline": "2026-09-25T18:00:00Z"
}
