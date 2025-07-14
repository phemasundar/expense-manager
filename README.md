# Personal Expense Manager

This project is a web application designed to help users track their grocery expenses. The application allows users to upload receipts, and using Optical Character Recognition (OCR), it automatically extracts individual items and their prices. This data is stored and can be analyzed to track spending habits and compare prices across different stores over time.

## Tech Stack

*   **Frontend**: React, Vite, TypeScript, Clerk
*   **Backend**: Java, Spring Boot, Gradle
*   **Database**: PostgreSQL

## Development Progress

### Epic 1: Project Foundation & Backend Setup (Completed)

*   Initialized a new Spring Boot project using Java 17 and Gradle.
*   Configured the Spring Boot application to connect to a local PostgreSQL database.
*   Created a simple "Health Check" REST controller.
*   Created the JPA entity classes.

### Epic 2: Frontend Setup & Basic UI (Completed)

*   Initialized a new React project using Vite.
*   Implemented a basic UI with a header, a main content area, and a footer.
*   Created a "Health Check" component that calls the backend's `/api/health` endpoint and displays the status.
*   Set up Clerk for user authentication.
