
# Finova Digital Banking & Wallet Platform

## API Documentation

**Base URL**

```text
http://localhost:8080
```

**Authentication**

Protected endpoints require a JWT access token:

```http
Authorization: Bearer <access_token>
```

---

# 1. AUTHENTICATION

Base path:

```text
/api/auth
```

Authentication endpoints handle registration, login, password recovery, OTP verification, and token management.

---

## 1.1 Register

Creates a new Finova customer account.

**Endpoint**

```http
POST /api/auth/register
```

**Request Body**

```json
{
  "firstName": "Johnson",
  "lastName": "Akamu",
  "email": "johnson@example.com",
  "phoneNumber": "+2348012345678",
  "password": "Password123"
}
```

**Success Response**

```http
201 Created
```

```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "firstName": "Johnson",
    "lastName": "Akamu",
    "email": "johnson@example.com",
    "phoneNumber": "+2348012345678",
    "role": "USER",
    "status": "ACTIVE"
  }
}
```

---

## 1.2 Login

Authenticates a customer and returns access and refresh tokens.

```http
POST /api/auth/login
```

**Request**

```json
{
  "email": "johnson@example.com",
  "password": "Password123"
}
```

**Response**

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJ...",
    "refreshToken": "eyJ...",
    "user": {}
  }
}
```

---

## 1.3 Refresh Token

Generates a new access token using a valid refresh token.

```http
POST /api/auth/refresh
```

**Request**

```json
{
  "refreshToken": "eyJ..."
}
```

---

## 1.4 Logout

Logs the authenticated customer out and revokes the refresh token.

```http
POST /api/auth/logout
```

**Request**

```json
{
  "refreshToken": "eyJ..."
}
```

**Authentication:** Required

---

## 1.5 Forgot Password

Initiates the password reset process.

```http
POST /api/auth/forgot-password
```

**Request**

```json
{
  "email": "johnson@example.com"
}
```

---

## 1.6 Reset Password

Resets the customer's password using a valid reset token.

```http
POST /api/auth/reset-password
```

**Request**

```json
{
  "token": "reset-token",
  "newPassword": "NewPassword123"
}
```

---

## 1.7 Verify OTP

Verifies an OTP sent to the customer.

```http
POST /api/auth/verify-otp
```

**Request**

```json
{
  "email": "johnson@example.com",
  "otp": "123456"
}
```

---

## 1.8 Resend Phone OTP

Resends an OTP to the customer's phone.

```http
POST /api/auth/resend-phone-otp
```

**Authentication:** Required

---

## 1.9 Verify Phone

Verifies the customer's phone number using an OTP.

```http
POST /api/auth/verify-phone
```

**Request**

```json
{
  "otp": "123456"
}
```

**Authentication:** Required

---

# 2. ACCOUNTS

Base path:

```text
/api/users/me/accounts
```

Account endpoints allow customers to create and manage their Finova bank accounts.

Supported account types may include:

```text
SAVING
CURRENT
FIXED
```

Supported account statuses:

```text
ACTIVE
BLOCKED
FROZEN
```

---

## 2.1 Create Account

Creates a new bank account for the authenticated customer.

```http
POST /api/users/me/accounts
```

**Request**

```json
{
  "accountType": "SAVING",
  "currency": "NGN"
}
```

**Authentication:** Required

---

## 2.2 Get My Accounts

Returns all accounts belonging to the authenticated customer.

```http
GET /api/users/me/accounts
```

**Authentication:** Required

---

## 2.3 Get My Account

Returns details of a specific account.

```http
GET /api/users/me/accounts/{accountNumber}
```

**Authentication:** Required

**Example**

```text
GET /api/users/me/accounts/1234567890
```

---

## 2.4 Freeze My Account

Freezes an account belonging to the authenticated customer.

```http
PATCH /api/users/me/accounts/{accountNumber}/freeze
```

**Authentication:** Required

A frozen account should not allow operations such as:

- Deposit
- Withdrawal
- Transfer

depending on the business rules implemented by Finova.

---

## 2.5 Get My Account Transactions

Returns transaction history for a specific account.

```http
GET /api/users/me/accounts/{accountNumber}/transactions
```

**Authentication:** Required

---

## 2.6 Get Balance

Returns the current balance of an account.

```http
GET /api/users/me/accounts/{accountNumber}/balance
```

**Authentication:** Required

**Example Response**

```json
{
  "accountNumber": "1234567890",
  "balance": 250000.00,
  "currency": "NGN"
}
```

---

# 3. ADDRESS

Base path:

```text
/api/users/me/address
```

Address endpoints allow customers to manage their residential/contact address.

---

## 3.1 Create Address

Creates an address for the authenticated customer.

```http
POST /api/users/me/address
```

**Request**

```json
{
  "street": "12 Allen Avenue",
  "city": "Ikeja",
  "state": "Lagos",
  "country": "Nigeria",
  "postalCode": "100001"
}
```

---

## 3.2 Get My Address

Returns the customer's current address.

```http
GET /api/users/me/address
```

---

## 3.3 Update My Address

Updates the customer's address.

```http
PUT /api/users/me/address
```

**Request**

```json
{
  "street": "15 Allen Avenue",
  "city": "Ikeja",
  "state": "Lagos",
  "country": "Nigeria",
  "postalCode": "100001"
}
```

---

## 3.4 Delete Address

Deletes the customer's address.

```http
DELETE /api/users/me/address
```

---

# 4. BENEFICIARIES

Base path:

```text
/api/users/me/beneficiaries
```

Beneficiaries allow customers to save accounts they frequently transfer money to.

---

## 4.1 Create Beneficiary

```http
POST /api/users/me/beneficiaries
```

**Request**

```json
{
  "name": "Samson Akaji",
  "accountNumber": "1234567890",
  "bankName": "Finova Bank"
}
```

---

## 4.2 Get Beneficiaries

Returns all beneficiaries belonging to the authenticated customer.

```http
GET /api/users/me/beneficiaries
```

---

## 4.3 Get Beneficiary

Returns a specific beneficiary.

```http
GET /api/users/me/beneficiaries/{id}
```

---

## 4.4 Update Beneficiary

Updates an existing beneficiary.

```http
PUT /api/users/me/beneficiaries/{id}
```

---

## 4.5 Delete Beneficiary

Deletes a beneficiary.

```http
DELETE /api/users/me/beneficiaries/{id}
```

---

# 5. TRANSACTIONS

Base path:

```text
/api/users/me/transactions
```

Transaction endpoints allow customers to deposit, withdraw, transfer funds, and retrieve statements.

Supported transaction types:

```text
DEPOSIT
WITHDRAWAL
TRANSFER
```

Supported transaction statuses:

```text
PENDING
SUCCESS
FAILED
```

---

## 5.1 Deposit

Deposits money into an account.

```http
POST /api/users/me/transactions/deposit
```

**Request**

```json
{
  "accountId": 1,
  "transactionType": "DEPOSIT",
  "amount": 50000,
  "description": "Cash deposit"
}
```

**Authentication:** Required

---

## 5.2 Withdraw

Withdraws money from an account.

```http
POST /api/users/me/transactions/withdraw
```

**Request**

```json
{
  "accountId": 1,
  "transactionType": "WITHDRAWAL",
  "amount": 10000,
  "description": "Cash withdrawal"
}
```

The withdrawal should fail if:

- Account does not belong to the customer
- Account is frozen/blocked
- Amount is invalid
- Account has insufficient funds

---

## 5.3 Transfer

Transfers money from the customer's account to another account.

```http
POST /api/users/me/transactions/transfer
```

**Request**

```json
{
  "accountId": 1,
  "destinationAccountNumber": "1234567890",
  "transactionType": "TRANSFER",
  "amount": 25000,
  "description": "Transfer to Samson"
}
```

The transfer should be transactional so that the debit and credit operations succeed or fail together.

---

## 5.4 Get Statement

Returns the customer's transaction statement.

```http
GET /api/users/me/transactions/statement
```

Possible query parameters:

```text
accountNumber
startDate
endDate
transactionType
status
page
size
```

**Example**

```http
GET /api/users/me/transactions/statement?accountNumber=1234567890&page=0&size=20
```

---

# 6. USER

Base path:

```text
/api/users/me
```

These endpoints allow an authenticated customer to manage their own profile.

---

## 6.1 Get Current User

Returns the currently authenticated customer's profile.

```http
GET /api/users/me
```

---

## 6.2 Update Current User

Updates customer profile information.

```http
PUT /api/users/me
```

**Request**

```json
{
  "firstName": "Johnson",
  "lastName": "Akamu"
}
```

---

## 6.3 Change Password

Changes the authenticated customer's password.

```http
PATCH /api/users/me/password
```

**Request**

```json
{
  "currentPassword": "OldPassword123",
  "newPassword": "NewPassword123"
}
```

---

## 6.4 Update Phone

Updates the customer's phone number.

```http
PATCH /api/users/me/phone
```

**Request**

```json
{
  "phoneNumber": "+2348012345678"
}
```

Phone verification/OTP may be required after changing the number.

---

## 6.5 Update Email

Updates the customer's email address.

```http
PATCH /api/users/me/email
```

**Request**

```json
{
  "email": "newemail@example.com"
}
```

Email verification may be required after changing the email address.

---

## 6.6 Update Address

Updates the customer's address.

```http
PUT /api/users/me/address
```

> This overlaps with the Address module's `updateMyAddress` endpoint. In the final API, use one endpoint rather than maintaining two separate endpoints for the same operation.

---

## 6.7 Delete Current User

Deletes/deactivates the customer's account.

```http
DELETE /api/users/me
```

For a banking system, this should preferably be implemented as a **soft delete/deactivation** rather than physically removing financial records.

---

# 7. ADMIN

Base path:

```text
/api/admin
```

Admin endpoints are restricted to users with the `ADMIN` role.

```http
Authorization: Bearer <admin_access_token>
```

---

## 7.1 Get All Users

Returns customers registered on the Finova platform.

```http
GET /api/admin/users
```

Possible filtering:

```http
GET /api/admin/users?status=BLOCKED
```

---

## 7.2 Find Customer

Allows customer service/admin staff to search for a customer.

```http
GET /api/admin/users/account-lookup
```

Possible parameters:

```text
firstName
lastName
phone
email
```

**Example**

```http
GET /api/admin/users/account-lookup?firstName=Johnson&lastName=Akamu
```

---

## 7.3 Get User By ID

Returns a specific customer's information.

```http
GET /api/admin/users/{id}
```

---

## 7.4 Update User Status

Updates the customer's account status.

```http
PATCH /api/admin/users/{id}/status
```

**Request**

```json
{
  "status": "BLOCKED"
}
```

Possible statuses:

```text
ACTIVE
BLOCKED
FROZEN
```

---

## 7.5 Delete User

Deletes/deactivates a customer.

```http
DELETE /api/admin/users/{id}
```

For a banking platform, this should preferably be a soft operation rather than physically deleting financial records.

---

# 8. HTTP STATUS CODES

Finova uses standard HTTP status codes.

| Status | Meaning |
|---|---|
| `200 OK` | Request completed successfully |
| `201 CREATED` | Resource created successfully |
| `204 NO CONTENT` | Request completed with no response body |
| `400 BAD REQUEST` | Invalid request |
| `401 UNAUTHORIZED` | Authentication required or token invalid |
| `403 FORBIDDEN` | Authenticated user does not have permission |
| `404 NOT FOUND` | Resource was not found |
| `409 CONFLICT` | Resource conflicts with existing data |
| `422 UNPROCESSABLE ENTITY` | Validation/business-rule failure |
| `500 INTERNAL SERVER ERROR` | Unexpected server error |

---

# 9. COMMON ERROR RESPONSE

Finova uses a consistent error response structure.

```json
{
  "timestamp": "2026-09-12T16:46:56",
  "message": "User already exists with email: johnsonakanmu@gmail.com",
  "path": "/api/auth/register",
  "errorCode": "CONFLICT"
}
```

Examples of application exceptions include:

```text
EmailAlreadyExistsException
PhoneNumberAlreadyExistsException
InvalidCredentialsException
AccountNotFoundException
InsufficientBalanceException
AccountBlockedException
AccountFrozenException
BeneficiaryNotFoundException
TransactionNotFoundException
InvalidRefreshTokenException
```

---

# 10. AUTHENTICATION FLOW

A typical customer flow is:

```text
Register
   ↓
Verify OTP / Phone
   ↓
Login
   ↓
Receive Access Token + Refresh Token
   ↓
Access protected endpoints
   ↓
Access Token expires
   ↓
Refresh Token
   ↓
Receive new Access Token
   ↓
Logout
   ↓
Refresh Token revoked
```

---

# 11. ACCOUNT & TRANSACTION FLOW

Typical banking operation:

```text
Customer
   ↓
Create Account
   ↓
Account Status = ACTIVE
   ↓
Deposit
   ↓
Balance increases
   ↓
Transfer / Withdrawal
   ↓
Transaction recorded
   ↓
Transaction History / Statement
```

For transfers:

```text
Source Account
      ↓
Validate ownership
      ↓
Validate ACTIVE status
      ↓
Validate sufficient balance
      ↓
Debit source account
      ↓
Credit destination account
      ↓
Create transaction records
      ↓
Return successful transaction
```

All money movement should be handled inside a database transaction using `@Transactional`.

---

# 12. SECURITY

Public endpoints:

```text
POST /api/auth/register
POST /api/auth/login
POST /api/auth/refresh
POST /api/auth/forgot-password
POST /api/auth/reset-password
POST /api/auth/verify-otp
```

Protected endpoints require:

```http
Authorization: Bearer <access_token>
```

Admin endpoints require:

```text
ROLE_ADMIN
```

Customer endpoints operate against the currently authenticated customer and should not accept a user ID from the client to determine ownership.

---

# 13. API MODULE SUMMARY

| Module | Endpoint | Method | Authentication |
|---|---|---|---|
| Auth | `/api/auth/register` | POST | Public |
| Auth | `/api/auth/login` | POST | Public |
| Auth | `/api/auth/refresh` | POST | Public |
| Auth | `/api/auth/logout` | POST | Required |
| Auth | `/api/auth/forgot-password` | POST | Public |
| Auth | `/api/auth/reset-password` | POST | Public |
| Auth | `/api/auth/verify-otp` | POST | Public |
| Auth | `/api/auth/resend-phone-otp` | POST | Required |
| Auth | `/api/auth/verify-phone` | POST | Required |
| Account | `/api/users/me/accounts` | POST | Required |
| Account | `/api/users/me/accounts` | GET | Required |
| Account | `/api/users/me/accounts/{accountNumber}` | GET | Required |
| Account | `/api/users/me/accounts/{accountNumber}/freeze` | PATCH | Required |
| Account | `/api/users/me/accounts/{accountNumber}/transactions` | GET | Required |
| Account | `/api/users/me/accounts/{accountNumber}/balance` | GET | Required |
| Address | `/api/users/me/address` | POST | Required |
| Address | `/api/users/me/address` | GET | Required |
| Address | `/api/users/me/address` | PUT | Required |
| Address | `/api/users/me/address` | DELETE | Required |
| Beneficiary | `/api/users/me/beneficiaries` | POST | Required |
| Beneficiary | `/api/users/me/beneficiaries` | GET | Required |
| Beneficiary | `/api/users/me/beneficiaries/{id}` | GET | Required |
| Beneficiary | `/api/users/me/beneficiaries/{id}` | PUT | Required |
| Beneficiary | `/api/users/me/beneficiaries/{id}` | DELETE | Required |
| Transaction | `/api/users/me/transactions/deposit` | POST | Required |
| Transaction | `/api/users/me/transactions/withdraw` | POST | Required |
| Transaction | `/api/users/me/transactions/transfer` | POST | Required |
| Transaction | `/api/users/me/transactions/statement` | GET | Required |
| User | `/api/users/me` | GET | Required |
| User | `/api/users/me` | PUT | Required |
| User | `/api/users/me/password` | PATCH | Required |
| User | `/api/users/me/phone` | PATCH | Required |
| User | `/api/users/me/email` | PATCH | Required |
| User | `/api/users/me/address` | PUT | Required |
| User | `/api/users/me` | DELETE | Required |
| Admin | `/api/admin/users` | GET | Admin |
| Admin | `/api/admin/users/account-lookup` | GET | Admin |
| Admin | `/api/admin/users/{id}` | GET | Admin |
| Admin | `/api/admin/users/{id}/status` | PATCH | Admin |
| Admin | `/api/admin/users/{id}` | DELETE | Admin |

---

# 14. PROJECT MODULES

The Finova backend is organized around the following modules:

```text
com.finova
├── auth
├── user
├── address
├── account
├── transaction
├── beneficiary
├── otp
├── notification
├── common
└── config
```

The API is designed around authenticated customers, secure account ownership, controlled financial transactions, OTP verification, JWT authentication, and administrator operations.

