# Personal Expense Manager

This project is a web application designed to help users track their grocery expenses. The application allows users to upload receipts, and using Optical Character Recognition (OCR), it automatically extracts individual items and their prices. This data is stored and can be analyzed to track spending habits and compare prices across different stores over time.

## Key Features

*   User authentication and profile management via Clerk.
*   Image upload functionality for grocery receipts.
*   Automated item and price extraction using the Google Cloud Vision API.
*   Manual editing of extracted entries for accuracy.
*   Historical view of all purchased items, filterable by date.
*   Price comparison and spending analytics.

## Architecture

We are starting with a **modular monolithic architecture**. This approach provides the simplicity of a single codebase and deployment unit while organizing the code in a structured, modular way. This modularity will significantly simplify a future migration to a microservices architecture if the need for scaling arises.

### Architectural Diagram

```
[Frontend (React Web App / Hybrid Android App)]
                      |
                      v
      [REST API / GraphQL API]
                      |
                      v
+-------------------------------------------+
|      Monolithic Backend (Java Spring Boot)      |
|                                           |
|  +---------------------------------------+  |
|  |             API Layer                 |  |
|  |  (Controllers / Request Handling)     |  |
|  +---------------------------------------+  |
|  |           Business Logic              |  |
|  |  (Service Modules: User, Receipt,     |  |
|  |   Data, Analytics)                    |  |
|  +---------------------------------------+  |
|  |           Data Access Layer           |  |
|  |  (JPA / Repositories)                 |  |
|  +---------------------------------------+  |
+-------------------------------------------+
            |               |
            v               v
[PostgreSQL Database]   [Google Cloud Vision API]
```

### Architectural Components

*   **Frontend**: A React-based single-page application (SPA).
*   **Backend Application**: A single, unified **Java Spring Boot** application, internally divided into logical modules (User, Receipt, Data, Analytics).
*   **Database**: A single **PostgreSQL** database.
*   **External Service**: The **Google Cloud Vision API** for OCR processing.

## Technology Stack

*   **Frontend**: **React.js** (with Vite), TypeScript
*   **Backend**: **Java 17+** with **Spring Boot 3**, Gradle
*   **Database**: **PostgreSQL**
*   **OCR Service**: **Google Cloud Vision API**
*   **Authentication**: **Clerk**
*   **CI/CD**: **GitHub Actions**
*   **Deployment**: **Docker**

## Database Schema

```sql
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    clerk_id VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stores (
    store_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255)
);

CREATE TABLE receipts (
    receipt_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    store_id INT REFERENCES stores(store_id),
    image_url VARCHAR(255) NOT NULL,
    purchase_date DATE NOT NULL,
    total_amount DECIMAL(10, 2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE items (
    item_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE receipt_items (
    receipt_item_id SERIAL PRIMARY KEY,
    receipt_id INT REFERENCES receipts(receipt_id),
    item_id INT REFERENCES items(item_id),
    quantity DECIMAL(10, 3),
    price DECIMAL(10, 2) NOT NULL
);
```

## API Endpoints

The Spring Boot application exposes a single, unified REST API under the `/api/v1` prefix.

*   **User Management:**
    *   `GET /users/me`: Get the current authenticated user's profile.
*   **Receipt Management:**
    *   `POST /receipts/upload`: Upload a new receipt image for OCR processing.
    *   `GET /receipts`: Get all receipts for the current user.
    *   `GET /receipts/{id}`: Get details for a specific receipt.
*   **Data Management:**
    *   `PUT /receipt-items/{id}`: Update a specific line item on a receipt.
    *   `DELETE /receipts/{id}`: Delete a receipt.

## CI/CD Pipeline (GitHub Actions)

The CI/CD pipeline is configured via GitHub Actions to automate the build, test, and deployment process.

1.  **On push to `main` branch**:
    *   Checkout code.
    *   Set up Java & Node.js environments.
    *   Build and test the backend.
    *   Build the frontend application.
    *   Dockerize the application into a single image.
    *   Push the image to a container registry.
    *   Trigger deployment.

## Development Plan

### Epic 1: Project Foundation & Backend Setup (Completed)
*   Initialized a new Spring Boot project using Java 17 and Gradle.
*   Configured the Spring Boot application to connect to a local PostgreSQL database.
*   Created a simple "Health Check" REST controller.
*   Created the JPA entity classes.

### Epic 2: Frontend Setup & Basic UI (Completed)
*   Initialized a new React project using Vite.
*   Implemented a basic UI with a header, a main content area, and a footer.
*   Created a "Health Check" component that calls the backend's `/api/health` endpoint.
*   Set up Clerk for user authentication.

### Epic 3: User Management & Authentication (Completed)
*   Created a protected route for a `/dashboard` page.
*   Created a `UserService` in the backend to sync user data from Clerk.

### Epic 4: Receipt Upload (UI and Backend Stub) (Completed)
*   Created the frontend upload form and image preview.
*   Created the backend `ReceiptController` to handle file uploads.
*   Connected the frontend form to the backend API endpoint.

### Epic 5: OCR Integration & Processing (Completed)
*   Added the Google Cloud Vision client library to the backend.
*   Created an `OcrService` to process images and extract text.
*   Modified the upload endpoint to call the `OcrService` and return the extracted text.

### Epic 6: Displaying and Correcting OCR Results (In Progress)
*   Redirect the user to a "Review Receipt" page after upload.
*   Created a backend endpoint to create receipt and store entries from the extracted data.

### Epic 7: API Documentation (Completed)
*   Generated Swagger documentation for the existing APIs.

## Future Plans

*   **Migration to Microservices**: As the application scales, break out modules into separate microservices.
*   **Cloud Database/Spreadsheet Integration**: Support for Google Sheets or other cloud databases.
*   **Advanced Analytics**: Introduce more sophisticated reports and spending trends.
*   **iOS & Android Application**: Build and deploy a hybrid mobile app.
*   **Barcode Scanning**: Add functionality to scan item barcodes for faster data entry.