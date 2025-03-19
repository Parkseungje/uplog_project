# Gradle 8.4 + JDK 17 사용 (Java 21과의 호환성 문제 해결)
FROM gradle:8.4-jdk17 AS builder

WORKDIR /app
COPY ./ ./
RUN gradle clean build --no-daemon

FROM openjdk:17.0-slim
WORKDIR /app
COPY --from=builder /app/build/libs/backend-0.0.1-SNAPSHOT.jar .

EXPOSE 8080
USER nobody
ENTRYPOINT ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]
