FROM node:20.9.0 AS frontendbuilder
WORKDIR /app
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci
COPY frontend/ .
WORKDIR /app/frontend
RUN npm run build


FROM maven:3.8.4-openjdk-17 AS backendbuilder
WORKDIR /app
COPY ./backend/pom.xml ./.
RUN mvn dependency:go-offline dependency:resolve-plugins
COPY backend/src /app/src
COPY --from=frontendbuilder /app/dist /app/src/main/resources/static
RUN mvn clean package


FROM eclipse-temurin:17.0.10_7-jre-ubi9-minimal@sha256:4a509b34e7ad76b4e705a9718b08f948a38d895815a93415ddd9595547bd6634
WORKDIR /usr/local/bin/
COPY --from=backendbuilder /app/target/secret-server-0.0.1-SNAPSHOT.jar secret.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "secret.jar"]

