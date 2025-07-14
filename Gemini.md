# Personal Expense Manager - Gemeni.md (Monolithic Architecture)

This document outlines the architecture, technology stack, and development plan for the Personal Expense Manager application, starting with a monolithic architecture for simplicity and rapid development.

## 1. Project Overview

The **Personal Expense Manager** is a web application designed to help users track their grocery expenses. The application will allow users to upload receipts, and using Optical Character Recognition (OCR), it will automatically extract individual items and their prices. This data will be stored and can be analyzed to track spending habits and compare prices across different stores over time.

### **Key Features:**

  * User authentication and profile management via Clerk.
  * Image upload functionality for grocery receipts.
  * Automated item and price extraction using the Google Cloud Vision API.
  * Manual editing of extracted entries for accuracy.
  * Historical view of all purchased items, filterable by date.
  * Price comparison and spending analytics.

## 2. Architecture

We will begin with a **modular monolithic architecture**. This approach provides the simplicity of a single codebase and deployment unit while organizing the code in a structured, modular way. This modularity will significantly simplify the future migration to a microservices architecture when the need for scaling arises.

### **Architectural Diagram**

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

### **Architectural Components:**

  * **Frontend**: A React-based single-page application (SPA) for the web UI and a hybrid mobile app built with a framework like React Native.
  * **Backend Application**: A single, unified **Java Spring Boot** application. Internally, it will be divided into logical modules to maintain a clean separation of concerns:
      * **User Module**: Manages user authentication logic (integrating with Clerk) and profile data.
      * **Receipt Module**: Handles receipt image uploads and coordinates with the external OCR service.
      * **Data Module**: Manages all CRUD (Create, Read, Update, Delete) operations for stores, items, and prices.
      * **Analytics Module**: Contains the business logic for generating insights, tracking prices, and comparing expenses.
  * **Database**: A single **PostgreSQL** database to store all application data.
  * **External Service**: The **Google Cloud Vision API** will be called by the Receipt Module for OCR processing.

## 3. Technology Stack

  * **Frontend**: **React.js** (with Vite or Create React App)
  * **Backend**: **Java 17+** with **Spring Boot 3**.
  * **Database**: **PostgreSQL**.
  * **OCR Service**: **Google Cloud Vision API**.
  * **Authentication**: **Clerk**.
  * **CI/CD**: **GitHub Actions**.
  * **Deployment**: **Docker**.

## 4. Project Structure

```
C:/Projects/ExpenseManager/
├───backend/
│   ├───.gradle/
│   ├───src/
│   │   ├───main/
│   │   │   ├───java/
│   │   │   │   └───com/
│   │   │   │       └───expensetracker/
│   │   │   │           └───expensetracker/
│   │   │   │               ├───ExpenseTrackerApplication.java
│   │   │   │               ├───controller/
│   │   │   │               │   └───HealthController.java
│   │   │   │               └───entity/
│   │   │   │                   ├───Item.java
│   │   │   │                   ├───Receipt.java
│   │   │   │                   ├───ReceiptItem.java
│   │   │   │                   ├───Store.java
│   │   │   │                   └───User.java
│   │   │   └───resources/
│   │   │       └───application.properties
│   │   └───test/
│   │       └───java/
│   │           └───com/
│   │               └───expensetracker/
│   │                   └───expensetracker/
│   │                       └───ExpenseTrackerApplicationTests.java
│   ├───build.gradle
│   ├───gradlew
│   ├───gradlew.bat
│   └───settings.gradle
├───frontend/
│   ├───node_modules/
│   ├───public/
│   │   └───vite.svg
│   ├───src/
│   │   ├───assets/
│   │   │   └───react.svg
│   │   ├───App.css
│   │   ├───App.tsx
│   │   ├───HealthCheck.tsx
│   │   ├───index.css
│   │   └───main.tsx
│   ├───.eslintrc.cjs
│   ├───.gitignore
│   ├───index.html
│   ├───package.json
│   ├───postcss.config.js
│   ├───README.md
│   ├───tsconfig.json
│   ├───tsconfig.node.json
│   └───vite.config.ts
├───Gemini.md
└───README.md
```

## 5. Database Schema (PostgreSQL)

The database schema remains the same, designed to hold all necessary data in a relational structure.

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

## 6. API Endpoints

The Spring Boot application will expose a single, unified REST API.

### **API Routes (`/api/v1`)**

  * **User Management:**
      * `GET /users/me`: Get the current authenticated user's profile.
  * **Receipt Management:**
      * `POST /receipts/upload`: Upload a new receipt image. The backend will handle processing.
      * `GET /receipts`: Get all receipts for the current user.
      * `GET /receipts/{id}`: Get details for a specific receipt, including its items.
  * **Data Management:**
      * `PUT /receipt-items/{id}`: Update a specific line item on a receipt (for corrections).
      * `DELETE /receipts/{id}`: Delete a receipt and its associated items.

## 7. CI/CD Pipeline (GitHub Actions)

The CI/CD pipeline with **GitHub Actions** will be streamlined for the monolithic architecture.

### **Workflow (`.github/workflows/main.yml`)**

1.  **On push to `main` branch**:
      * **Checkout Code**: Pull the latest code from the repository.
      * **Setup Java & Node**: Set up the required JDK and Node.js environments.
      * **Build & Test Backend**: Compile the Java code, run all tests (unit and integration), and package the application into an executable JAR file.
      * **Build Frontend**: Run `npm install` and `npm run build` for the React application.
      * **Dockerize**: Create a single Docker image that contains the Spring Boot JAR file and serves the static frontend assets.
      * **Push to Registry**: Push the final Docker image to a container registry (e.g., Docker Hub, GitHub Container Registry).
      * **Deploy**: Trigger a deployment of the new container to a hosting service (e.g., a VPS, AWS EC2, or a platform like Heroku/Render).

## 8. Development Plan

### Epic 1: Project Foundation & Backend Setup (Completed)

*   **Task 1.1: Initialize a new Spring Boot project using Java 17 and Gradle.**
    *   Dependencies: Spring Web, Spring Data JPA, PostgreSQL Driver, Spring Boot DevTools.
*   **Task 1.2: Configure the Spring Boot application to connect to a local PostgreSQL database.**
    *   Copied database configuration from the root `application.properties` file.
*   **Task 1.3: Create a simple "Health Check" REST controller.**
    *   GET endpoint at `/api/health` that returns `{"status": "UP"}`.
*   **Task 1.4: Create the JPA entity classes.**
    *   Created `User`, `Store`, `Receipt`, `Item`, and `ReceiptItem` entities with appropriate annotations.

### Epic 2: Frontend Setup & Basic UI (Completed)

*   **Task 2.1: Initialize a new React project using Vite.** (Completed)
*   **Task 2.2: Implement a basic UI with a header, a main content area, and a footer.** (Completed)
*   **Task 2.3: Create a "Health Check" component that calls the backend's `/api/health` endpoint and displays the status.** (Completed)
*   **Task 2.4: Set up Clerk for user authentication.** (Completed)

## 9. Future Plans

  * **Migration to Microservices**: As the application scales, the modular design of the monolith will allow us to break out individual modules (e.g., Analytics, User management) into separate microservices with minimal friction.
  * **Cloud Database/Spreadsheet Integration**: Support for Google Sheets or other cloud databases as a data source or destination.
  * **Advanced Analytics**: Introduce more sophisticated reports, spending trends, and personalized saving suggestions.
  * **iOS & Android Application**: Build and deploy the hybrid app for iOS & Android using the React Native codebase.
  * **Barcode Scanning**: Add functionality to scan item barcodes for faster and more accurate data entry.