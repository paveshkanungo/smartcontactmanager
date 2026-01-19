# Smart Contact Manager

A Spring Boot application for managing contacts efficiently.

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 4.x**
- **Thymeleaf** (Frontend)
- **MySQL** (Database)
- **Spring Security** (Authentication & Authorization)

## ✨ Features

- **User Authentication**: Secure Login/Signup with CSRF protection.
- **Dashboard**: Smart sidebar navigation and profile summary.
- **Contact Management**:
  - **Add Contact**: Form with server-side validation and image upload.
  - **View Contacts**: Paginated list view (5 contacts per page).
  - **Contact Details**: Individual contact view with option to update or delete.
  - **Update Contact**: Secure form to edit contact details and replace profile images.
  - **Delete Contact**: Secure deletion with JS confirmation popup (deletes image from server).
- **Profile View**: Dedicated user profile page.
- **Security**:
  - IDOR protection (users can only access their own data).
  - Session-based message handling (SweetAlert/Bootstrap alerts).

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
