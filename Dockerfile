# Stage 1: Build Java project
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven files and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build fat JAR
RUN mvn package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the fat JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (Render uses PORT env variable)
EXPOSE 10000
ENV PORT 10000

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]