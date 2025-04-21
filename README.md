#  API Automation Testing Project

This is a real-world **API automation testing framework** built using **Java, Rest Assured, and TestNG**. It covers complete CRUD operations with dynamic test validation, clean structure, and CI/CD integration via GitHub Actions.

>  Designed to simulate a mini backend product and user management system.  
>  Built as a portfolio-grade project to showcase skills in automation, API testing, and test architecture.

---

##  Project Structure

APIAssignment/ │ src/test/java/com/edureka/APIAssignment/ │ ProductAPITest.java # Tests product CRUD operations on a custom server │ UserManagementAPITesting.java # Tests user creation, validation, and login on fakestoreapi.com │ src/test/resources/ │ Addnewproduct.json # Payload for adding a new product │ JsonFiles.json # Payload for creating a new user │ .github/workflows/ │ ci.yml # GitHub Actions CI pipeline for running tests │ README.md # This file!



---

##  Tech Stack

- **Java** – Programming language
- **Rest Assured** – API testing framework
- **TestNG** – Test runner
- **GitHub Actions** – CI/CD pipeline
- **JSON** – Test payloads
- **Node.js Express Server** – Simulated backend for product tests

---

##  Features

-  **Automated API test cases** for both products and users
-  External JSON files for clean payload handling
-  Chained requests using extracted data (e.g., `productId`)
-  Response validation using **Hamcrest matchers**
-  Real API calls to [`https://fakestoreapi.com`](https://fakestoreapi.com)
-  Local mock server to simulate CRUD
-  Modular, maintainable test classes
-  **CI/CD**: Automatic test runs on every push

---

##  API Test Modules

### 1. **Product API Tests** (`ProductAPITest.java`)

Tests a local server (`http://localhost:3000`) with endpoints like `/products`.

- **Setup**: Clears previous products and creates a new one.
- `createNewProduct`: Sends POST request and stores the returned product ID.
- `getProductDetails`: Uses the stored ID to validate product retrieval.

### 2. **User Management API Tests** (`UserManagementAPITesting.java`)

Targets `https://fakestoreapi.com` and covers:

- `CreateNewUser`: Sends JSON payload to register a new user.
- `ValidateNewUser`: GET call to check user by ID.
- `LoginNewUser`: POST to `/auth/login` with username/password, validates token.

---

##  How to Run the Tests

### ▶ Pre-requisites

- Java 8+
- Maven
- TestNG
- Node.js (for mock server)
- Internet (for Fakestore API)

###  Steps

```bash

# 1. Clone the project
git clone https://github.com/your-username/API-Automation-Project.git

# 2. Navigate to project
cd API-Automation-Project

# 3. (Optional) Start local server for products
node server.js

# 4. Run tests using Maven
mvn test

⚙ CI/CD Pipeline (GitHub Actions)
Runs test suite on every push and pull_request

Uses a matrix to run on multiple Java versions

Fails the pipeline if any test fails

Logs are saved and accessible from GitHub > Actions tab.


