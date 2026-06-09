FROM gradle:8-jdk17 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew :account-service:bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/account-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
