FROM maven:3.6.3-jdk-11 as BUILD

RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src

RUN mvn clean package -DskipTests

FROM adoptopenjdk:11-jre-hotspot

COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]