#!/bin/bash
# This script compiles the project
cd /media/elishama/New\ Volume/project/Test/testApi
echo "Cleaning project..."
mvn clean
echo "Compiling project..."
mvn compile