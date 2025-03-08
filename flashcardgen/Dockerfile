# Use official Maven image for building the application
FROM maven:3.8.7-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .

# Download dependencies (this helps with caching)
RUN mvn dependency:go-offline

# Copy the rest of the project files
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use official JDK image for running the application
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy built JAR from previous stage
COPY --from=build /app/target/*.jar app.jar


# Expose application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]


