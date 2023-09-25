FROM openjdk:17
ARG JAR_FILE=impl/target/social-service-impl-1.0.0-SNAPSHOT-exec.jar
COPY ${JAR_FILE} social-service-impl.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/social-service-impl.jar"]