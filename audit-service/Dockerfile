FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/audit-service-0.0.1-SNAPSHOT.jar /app/audit-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/audit-service.jar"]
