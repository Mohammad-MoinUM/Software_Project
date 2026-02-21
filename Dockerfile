FROM ubuntu:latest
LABEL authors="appreel"

ENTRYPOINT ["top", "-b"]

# =============================================================================
# STAGE 1: BUILD STAGE (Compile and package the application)
# =============================================================================
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy Maven configuration files first (for better caching)
COPY pom.xml .

# Download dependencies (cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests for production build)
RUN mvn clean package -DskipTests -B

# =============================================================================
# STAGE 2: RUNTIME STAGE (Create lightweight runtime image)
# =============================================================================
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Create non-root user for security (professional best practice)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Expose the application port
EXPOSE 8080

# Health check (tells Docker if app is healthy)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]