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

### Epic 3: User Management & Authentication (Completed)

*   Created a protected route for a /dashboard page that redirects to a sign-in page if the user is not logged in.
*   Created a `UserService` in the Spring Boot backend that, upon a user's first authenticated API call, saves their Clerk userId and email into the users table.

### Epic 4: Receipt Upload (UI and Backend Stub) (Completed)

*   In the React frontend, created a new page at `/upload` containing a form with a file input that accepts only image files (`image/*`).
*   When a user selects a file, a preview of the image is displayed on the page.
*   In the Spring Boot backend, created a new REST controller `ReceiptController` with a POST endpoint at `/api/v1/receipts/upload`.
*   Connected the React upload form to the backend to send the image file to the `/api/v1/receipts/upload` endpoint.
