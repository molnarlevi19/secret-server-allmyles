FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml ./.

RUN mvn dependency:go-offline dependency:resolve-plugins

COPY src ./src

RUN mvn clean package

FROM eclipse-temurin:17.0.10_7-jre-ubi9-minimal@sha256:4a509b34e7ad76b4e705a9718b08f948a38d895815a93415ddd9595547bd6634

WORKDIR /usr/local/bin/

COPY --from=build /app/target/secret-server-0.0.1-SNAPSHOT.jar secret.jar

ENTRYPOINT ["java", "-jar", "secret.jar"]