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

# Flags de JVM optimizados para entornos con memoria limitada (Railway/Render free tier).
# -XX:+UseContainerSupport: respeta los límites de memoria del contenedor en lugar de leer RAM total del host.
# -XX:MaxRAMPercentage=75.0: usa máximo el 75% de la RAM asignada al contenedor para el heap.
# -XX:+UseSerialGC: usa el GC más liviano (recomendado para servicios con < 512MB RAM).
# -Djava.security.egd: acelera el arranque en entornos sin entropía de hardware suficiente.
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseSerialGC", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
