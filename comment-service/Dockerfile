FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/comment-service-0.0.1-SNAPSHOT.jar /app/comment-service.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/app/comment-service.jar"]
