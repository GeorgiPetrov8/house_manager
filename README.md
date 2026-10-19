# House Manager

House Manager is a Java application for managing residential buildings. It keeps track of buildings, apartments, residents, and the fees and payments associated with them, storing all data in a MySQL database.

## Features

- Manage buildings, apartments, and residents
- Record and track payments (monthly fees, maintenance, etc.)
- View payment history and outstanding balances
- Persistent storage of all data in a relational database
- Application logging for diagnostics

## Technology

- **Language:** Java
- **Build tool:** Gradle (with Gradle Wrapper)
- **ORM:** Hibernate Core 5.6.15
- **Database:** MySQL (via MySQL Connector/J 8.0.33)
- **Logging:** Apache Log4j 2.20
- **Testing:** JUnit 5 (Jupiter)

## Project Structure

```
house_manager/
├── src/main/        # Application source code
├── payments/        # Payments-related files
├── build.gradle     # Gradle build configuration
├── settings.gradle
└── gradlew(.bat)    # Gradle wrapper scripts
```

## Getting Started

### Prerequisites

- JDK installed
- A running MySQL server

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/GeorgiPetrov8/house_manager.git
   cd house_manager
   ```
2. Create a MySQL database and configure the connection (URL, username, password) in the Hibernate configuration.
3. Build and run:
   ```bash
   ./gradlew build
   ./gradlew run
   ```
   On Windows use `gradlew.bat`.

### Run tests

```bash
./gradlew test
```
