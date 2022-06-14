FROM maven:3.6.3 as build

WORKDIR /app

COPY pom.xml /app
COPY src /app/src
RUN mvn clean package


FROM openjdk:15-jdk-slim
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java"]

CMD [ "-jar", "app.jar" ]