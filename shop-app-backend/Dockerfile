# Build stage
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build-stage
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Pprod -Dmaven.test.skip

FROM openjdk:21-jdk AS production-stage
WORKDIR /app
COPY --from=build-stage /home/app/target/shop-app-backend.jar .
ENV PORT=8088
EXPOSE 8088
CMD ["java", "-jar","-Dspring.profiles.active=prod", "shop-app-backend.jar"]