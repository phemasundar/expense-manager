# Gemini Context: Personal Expense Manager

## Project Overview
A web app for tracking grocery expenses by uploading and processing receipts with OCR.

## Architecture
- **Type:** Modular Monolith
- **Backend:** Java 17, Spring Boot 3
- **Frontend:** React (Vite)
- **Database:** PostgreSQL

## Technology Stack
- **Backend:** Java 17, Spring Boot 3, Gradle, JPA/Hibernate
- **Frontend:** React, TypeScript, Vite
- **Authentication:** Clerk
- **OCR:** Google Cloud Vision API
- **Deployment:** Docker, GitHub Actions

## Key Commands
- **Build Backend:** `cd backend && gradlew build`
- **Run Backend:** `cd backend && gradlew bootRun`
- **Build Frontend:** `cd frontend && npm install && npm run build`
- **Run Frontend Dev Server:** `cd frontend && npm run dev`

## Conventions
- **Java:** Use Lombok annotations (`@Data`, `@RequiredArgsConstructor`, etc.) for all new classes to minimize boilerplate.

## API Endpoints (`/api/v1`)
- `GET /users/me`: Get current user profile.
- `POST /receipts/upload`: Upload receipt for OCR processing.
- `POST /receipts`: Create a receipt record.
- `GET /receipts/{id}`: Get receipt details.

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
