# Smart Contact Manager

A Spring Boot application for managing contacts efficiently.

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 4.x**
- **Thymeleaf** (Frontend)
- **MySQL** (Database)
- **Spring Security**

## 🚀 Getting Started

### Prerequisites

- Java 17+ installed
- MySQL installed and running

### ⚙️ Configuration

This project uses a `.env` file for sensitive configuration (like database passwords).

1.  **Clone the repository**
2.  **Create a `.env` file** in the root directory:
    ```bash
    touch .env
    ```
3.  **Add your secrets** to the `.env` file:

    ```properties
    DB_PASSWORD=your_mysql_password
    ```

    > **Note:** The application uses `spring.config.import=optional:file:.env[.properties]` to automatically load these values.

### 🏃‍♂️ Running the App

You can run the application using Maven:

```bash
./mvnw spring-boot:run
```

The application will start at `http://localhost:8080`.

## 📦 Project Structure

- `src/main/java`: Source code
- `src/main/resources`: Configuration and templates
- `src/test`: Unit and integration tests
