FROM maven:3-amazoncorretto-11 AS build
COPY . /home/maven/src
WORKDIR /home/maven/src
RUN mvn package

FROM amazoncorretto:11-alpine3.13
EXPOSE 80:80
RUN mkdir /app
COPY --from=build /home/maven/src/target/*-with-dependencies.jar /app/ktor-docker-sample.jar
ENTRYPOINT ["java","-jar","/app/ktor-docker-sample.jar"]