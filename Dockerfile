# Multi-stage Dockerfile for Proyecto_Posgrados_backend
# Build with Maven and run with a lightweight JRE

# Build stage
FROM maven:3.9.16-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . /workspace
# Use the project's maven wrapper if you prefer: ./mvnw -B -DskipTests clean package
RUN mvn -B -DskipTests clean package

# Runtime stage
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
ARG JAR_FILE=platform/application/target/*.jar
COPY --from=build /workspace/${JAR_FILE} /app/app.jar
# Do NOT copy `.env.properties` into the image. Environment should be provided at runtime
# via bind-mount (`-v`) or container environment variables. This keeps secrets out
# of the image and matches Railway behavior where variables are set externally.
EXPOSE 8080
ENV PORT=8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Dserver.port=${PORT} -jar /app/app.jar"]
