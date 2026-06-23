FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/quiz-service*.jar app.jar
EXPOSE 8082 8084
ENTRYPOINT ["java","-jar","app.jar"]