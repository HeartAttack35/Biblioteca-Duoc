# ─────────────────────────────────────────────
# Etapa 1: Build
# Compila el proyecto y genera el JAR ejecutable
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar primero solo el pom.xml para aprovechar la caché de capas de Docker.
# Si las dependencias no cambiaron, Maven no las vuelve a descargar.
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -q

# Copiar el código fuente y compilar
COPY src src
RUN ./mvnw package -DskipTests -q

# ─────────────────────────────────────────────
# Etapa 2: Runtime
# Imagen final liviana, solo con el JRE necesario
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar solo el JAR generado desde la etapa de build
COPY --from=build /app/target/ms-auth-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
