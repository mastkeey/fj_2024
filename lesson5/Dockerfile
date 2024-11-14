FROM openjdk:17-jdk-slim

WORKDIR /app

COPY lesson5/build/libs/lesson5.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]