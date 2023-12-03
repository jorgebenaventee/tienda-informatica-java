FROM gradle:jdk17-alpine AS build

WORKDIR /app
COPY build.gradle.kts .
COPY gradlew .
COPY gradle gradle
COPY src src

RUN gradle build

FROM eclipse-temurin:17-jre-alpine AS run

WORKDIR /app

COPY --from=build /app/build/libs/*SNAPSHOT.jar /app/tienda-app.jar

EXPOSE 3000

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app/tienda-app.jar"]