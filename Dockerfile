FROM gradle:8.10.2-jdk21 AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src

RUN ./gradlew bootJar --no-daemon -x test


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8085

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
