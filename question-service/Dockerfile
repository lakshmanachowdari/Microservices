FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/question-service*.jar app.jar
EXPOSE 8081 8085
ENTRYPOINT ["java","-jar","app.jar"]