FROM maven:3.8-eclipse-temurin-21-alpine as builder

WORKDIR /home

ARG GITHUB_USERNAME
ARG GITHUB_TOKEN
ARG GITHUB_ORG

ENV GITHUB_USERNAME=$GITHUB_USERNAME
ENV GITHUB_TOKEN=$GITHUB_TOKEN
ENV GITHUB_ORG=$GITHUB_ORG

COPY ./jpo-geojsonconverter/pom.xml ./jpo-geojsonconverter/
COPY ./jpo-geojsonconverter/settings.xml ./jpo-geojsonconverter/
COPY ./jpo-geojsonconverter/src ./jpo-geojsonconverter/src

WORKDIR /home/jpo-geojsonconverter

RUN mvn -s settings.xml clean install -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /home

COPY --from=builder /home/jpo-geojsonconverter/src/main/resources/application.yaml /home
COPY --from=builder /home/jpo-geojsonconverter/src/main/resources/logback.xml /home
COPY --from=builder /home/jpo-geojsonconverter/target/jpo-geojsonconverter.jar /home

ENTRYPOINT ["java", \
	"-Djava.rmi.server.hostname=$DOCKER_HOST_IP", \
	"-Dcom.sun.management.jmxremote.port=9090", \
	"-Dcom.sun.management.jmxremote.rmi.port=9090", \
	"-Dcom.sun.management.jmxremote", \
	"-Dcom.sun.management.jmxremote.local.only=true", \
	"-Dcom.sun.management.jmxremote.authenticate=false", \
	"-Dcom.sun.management.jmxremote.ssl=false", \
	"-Dlogback.configurationFile=/home/logback.xml", \
	"-jar", \
	"/home/jpo-geojsonconverter.jar"]