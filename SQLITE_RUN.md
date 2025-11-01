# Running the Application with SQLite

This document explains how to run the TestApi application using SQLite as the database.

## Default Configuration

The application is configured by default to use SQLite. No additional setup is required to run with SQLite.

## Running the Application

### Method 1: Using Maven Wrapper (Recommended)

```bash
./mvnw spring-boot:run
```

### Method 2: Using the provided script

```bash
./run_default.sh
```

### Method 3: Build and run

```bash
./mvnw clean package
java -jar target/Association-soft-0.0.1-SNAPSHOT.jar
```

## Database Information

When running with SQLite:
- A file named `association.db` will be created in the project root directory
- The database schema will be automatically created and updated by Hibernate
- All data will be persisted in this file

## Configuration Files

The SQLite configuration is defined in:
- `src/main/resources/application.properties` (default profile)
- `src/main/resources/application.yml` (also default profile)

Key configuration properties:
```properties
spring.datasource.url=jdbc:sqlite:./association.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```

## Switching to MariaDB/MySQL

If you want to run with MariaDB or MySQL instead:
1. Use the production profile: `export SPRING_PROFILES_ACTIVE=prod`
2. Or run: `./run_prod.sh`
3. Make sure to configure the database connection in `application-prod.properties`

## Troubleshooting

If you encounter any issues:
1. Make sure the `association.db` file has proper write permissions
2. Delete the `association.db` file and restart the application to start fresh
3. Check the console output for any error messages