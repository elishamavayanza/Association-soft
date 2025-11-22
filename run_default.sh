#!/bin/bash
# Script to run the application with SQLite database (default profile)

cd /media/elishama/New\ Volume/project/Test/testApi

echo "Running application with SQLite database (default profile)..."
echo "Database file will be created as 'association.db' in the project root directory."
export SPRING_PROFILES_ACTIVE=default
./mvnw spring-boot:run