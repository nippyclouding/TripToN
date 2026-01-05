FROM openjdk:21-jdk-slim

WORKDIR /app

# Gradle wrapper와 build.gradle 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 의존성 다운로드 (캐시 최적화)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew bootJar --no-daemon

# 실행 포트
EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "build/libs/TripToN-0.0.1-SNAPSHOT.jar"]