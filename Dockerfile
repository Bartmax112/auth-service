FROM openjdk:16-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 4444
ENTRYPOINT ["java","-jar","/app.jar"]