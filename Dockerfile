FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/message_microservice-1.0-SNAPSHOT.jar
WORKDIR /opt/messages-ms
COPY ${JAR_FILE} messages-ms.jar
EXPOSE 8091
CMD ["java", "-jar", "messages-ms.jar"]
