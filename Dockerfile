FROM gradle:7.1.1-jdk11 AS build
WORKDIR /workspace/app
COPY . .
RUN gradle clean build

FROM openjdk:11-jdk-slim
WORKDIR /app
COPY --from=build /workspace/app/build/libs/account.manager-0.0.1-SNAPSHOT.jar .
COPY --from=build /workspace/app/database ./database
EXPOSE 8080
ENTRYPOINT ["java","-jar","account.manager-0.0.1-SNAPSHOT.jar"]