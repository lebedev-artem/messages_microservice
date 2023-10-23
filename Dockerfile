# Install Docker Images for OpenJDK Version 11 binaries built by AdoptOpenJDK. https://hub.docker.com/r/adoptopenjdk/openjdk11
FROM adoptopenjdk/openjdk11

# Specify a label. For example, image developer, etc. Optional item.
LABEL artifact="messages-micro-service"
LABEL version="0.1.0-SNAPSHOT"
LABEL maintainer="Artem Lebedev"
LABEL email="artlebedev2006@gmail.com"

# Specify a mount point for external data inside the container (as we remember, this is Linux)
VOLUME /app/backend
# The external port on which our application will be accessed from the outside
EXPOSE 8091

# Specify where the *.jar file is located in our application
ARG JAR_FILE=target/messages-0.1.0-SNAPSHOT.jar

# Add *.jar file to the image under the name messages.jar
ADD ${JAR_FILE} messages.jar

# JAR run command
ENTRYPOINT ["java","-jar","messages.jar"]
