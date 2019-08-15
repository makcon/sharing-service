FROM adoptopenjdk/openjdk11:alpine-slim

COPY sharing-service-ws/target/sharing-service-ws-*.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]
