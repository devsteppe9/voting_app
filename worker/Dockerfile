# syntax=docker/dockerfile:1
# Use a base image with Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set the working directory in the image
WORKDIR /build

# Copy the pom.xml and dependency files
COPY pom.xml .
COPY ./src ./src

# Build the application
RUN mvn clean install -DskipTests

# Use a smaller base image for the final image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory in the image
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /build/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
