# ===== Stage 1: Build the JAR using the Maven Wrapper =====
# Java 17 matches <java.version>17</java.version> in pom.xml (Spring Boot 4.0 baseline).
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy the Maven wrapper first so dependency resolution can be cached
# independently of source changes.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

# Now copy the sources and build the executable JAR (skip tests for image build).
COPY src/ src/
RUN ./mvnw -B clean package -DskipTests

# ===== Stage 2: Lightweight runtime image =====
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Run as a non-root user.
RUN useradd --system --no-create-home --uid 1001 appuser

# Copy only the built application JAR from the build stage.
COPY --from=build /app/target/*.jar app.jar

USER appuser
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]