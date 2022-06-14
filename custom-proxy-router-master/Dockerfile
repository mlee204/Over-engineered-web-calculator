FROM maven:3.6.3-openjdk-15-slim as build

WORKDIR /app

COPY pom.xml /app
COPY src /app/src

RUN mvn package

FROM openjdk:15-jdk-slim
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar" ]
