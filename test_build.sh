#!/bin/bash
# Script to test the application

cd /media/elishama/New\ Volume/project/Test/testApi

echo "Cleaning previous builds..."
./mvnw clean

echo "Compiling the project..."
./mvnw compile

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Building the project..."
    ./mvnw package
    
    if [ $? -eq 0 ]; then
        echo "Build successful!"
        echo "To run with SQLite (default): ./run_default.sh"
        echo "To run with MariaDB (production): ./run_prod.sh"
        echo "Or run directly with: ./mvnw spring-boot:run"
    else
        echo "Build failed!"
    fi
else
    echo "Compilation failed!"
fi