FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/delivery-0.0.2-SNAPSHOT.jar .
EXPOSE 8099
ENTRYPOINT ["java","-jar","delivery-0.0.2-SNAPSHOT.jar"]