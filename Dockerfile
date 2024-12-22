FROM eclipse-temurin:21.0.5_11-jdk-ubi9-minimal
WORKDIR /cont
COPY ./pom.xml /cont
ADD ./target/L-Airline-0.0.4-SNAPSHOT.jar /jwtuserms.jar
COPY ./src /cont/src
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "/jwtuserms.jar"]