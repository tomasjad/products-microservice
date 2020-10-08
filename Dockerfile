FROM openjdk:11
ARG JAR_FILE=target/products-microservice.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
