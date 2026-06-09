FROM gradle:8-jdk17 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew :config-server:bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/config-server/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
