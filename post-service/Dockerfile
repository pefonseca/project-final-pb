FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/post-service-0.0.1-SNAPSHOT.jar /app/post-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app/post-service.jar"]
