FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY build/libs/social-api-1.0.0.jar build/

WORKDIR /app/build
ENTRYPOINT java -jar social-api-1.0.0.jar
