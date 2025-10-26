# ----------------------------
# Stage 1: Build
# ----------------------------
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copia pom.xml e mvnw para baixar dependências offline
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copia todo o código
COPY src ./src

# Build do jar
RUN ./mvnw package -DskipTests

# ----------------------------
# Stage 2: Runtime
# ----------------------------
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Copia o jar da fase de build
COPY --from=build /app/target/carekeeper-*.jar app.jar

EXPOSE 8080

# Comando de execução
CMD ["java", "-jar", "app.jar"]
