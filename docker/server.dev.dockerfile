FROM eclipse-temurin:21-jdk-alpine AS dev

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copia código para o container (pode usar volume no compose)
COPY src ./src

USER root

EXPOSE 8080

# Run the Spring Boot application with DevTools enabled
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.fork=false"]
