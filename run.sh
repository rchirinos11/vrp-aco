#!/bin/bash

echo "Startup"

cd /home/arch/vrp-aco
./mvnw clean install
java -jar target/vrp-0.0.1-SNAPSHOT.jar
