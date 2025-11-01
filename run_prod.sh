#!/bin/bash
# Script to run the application with MariaDB database (production profile)

cd /media/elishama/New\ Volume/project/Test/testApi

echo "Running application with MariaDB database (production profile)..."
echo "Make sure MariaDB is running and database is configured."
export SPRING_PROFILES_ACTIVE=prod
./mvnw spring-boot:run