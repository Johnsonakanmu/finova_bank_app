# finova_bank_app

## Finova Digital Banking Wallet

A secure digital banking and wallet platform built with
Java, Spring Boot, PostgreSQL and JWT authentication.

## Features

- User registration and authentication
- JWT access and refresh tokens
- OTP verification
- Password reset
- User profile management
- Multiple bank accounts
- Multi-currency accounts
- Deposits
- Withdrawals
- Internal transfers
- Beneficiary management
- Account status management
- Admin user management
- Transaction history
- Global exception handling
- Email notifications

## Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- PostgreSQL
- Maven
- Docker
- JavaMailSender
- Swagger

## API Documentation

### Authentication

#### Register User

`POST /api/auth/register`

**Request**

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+2348012345678",
  "password": "Password123"
}

#### Login User

`POST /api/auth/login`

**Request**

```json
{
  "email": "john@example.com",
  "password": "Password123"
}

