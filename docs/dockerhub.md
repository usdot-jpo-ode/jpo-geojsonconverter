# geojsonconverter

## GitHub Repository Link
https://github.com/usdot-jpo-ode/jpo-geojsonconverter

## Purpose
The JPO Intersection GeoJSON Converter is a real-time validator and data converter of JPO-ODE MAP and SPaT JSON based on the SAE J2735 message standard.

## How to pull the latest image
The latest image can be pulled using the following command:
> docker pull usdotjpoode/geojsonconverter:develop

## Required environment variables
- DOCKER_HOST_IP
- geometry.output.mode

## Direct Dependencies
- Kafka
- Zookeeper (relied on by Kafka)

## Indirect Dependencies
The GeoJSON Converter will not receive messages to process if the ODE is not running.

## Example docker-compose.yml with direct dependencies:
```
version: '3'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: ${DOCKER_HOST_IP}
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_CREATE_TOPICS: "topic.ProcessedSpat:1:1,topic.ProcessedMap:1:1,topic.ProcessedMapWKT:1:1"
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    depends_on:
      - zookeeper

  geojsonconverter:
    image: usdotjpoode/geojsonconverter:release_q3
    environment:
      DOCKER_HOST_IP: ${DOCKER_HOST_IP}
      geometry.output.mode: ${GEOMETRY_OUTPUT_MODE}
      spring.kafka.bootstrap-servers: ${DOCKER_HOST_IP}:9092
    logging: 
      options:
        max-size: "10m"
        max-file: "5"
    depends_on:
      - kafka
```

## Expected startup output
The logs should look like this:
```
geojsonconverter-geojsonconverter-1  | 2023-11-09 23:10:32 [main] INFO  JsonConverterServiceController - Creating the Processed MAP Kafka-Streams topology
geojsonconverter-geojsonconverter-1  | 2023-11-09 23:10:32 [main] INFO  JsonConverterServiceController - Creating the Processed SPaT Kafka-Streams topology
geojsonconverter-geojsonconverter-1  | 2023-11-09 23:10:32 [main] INFO  JsonConverterServiceController - All geoJSON conversion services started!
geojsonconverter-geojsonconverter-1  | 2023-11-09 23:10:32 [main] INFO  KafkaConfiguration - KafkaAdmin property bootstrap.servers = [192.168.0.243:9092]
geojsonconverter-geojsonconverter-1  | 2023-11-09 23:10:32 [main] INFO  KafkaConfiguration - Creating topics: [{name=topic.ProcessedSpat, cleanupPolicy=delete, retentionMs=300000}, {name=topic.ProcessedMap, cleanupPolicy=delete, retentionMs=300000}, {name=topic.ProcessedMapWKT, cleanupPolicy=delete, retentionMs=300000}]
geojsonconverter-geojsonconverter-1  | 2023-11-09 23:10:32 [main] INFO  KafkaConfiguration - New Topic: (name=topic.ProcessedSpat, numPartitions=1, replicationFactor=1, replicasAssignments=null, configs={cleanup.policy=delete, retention.ms=300000})
geojsonconverter-geojsonconverter-1  | 2023-11-09 23:10:32 [main] INFO  KafkaConfiguration - New Topic: (name=topic.ProcessedMap, numPartitions=1, replicationFactor=1, replicasAssignments=null, configs={cleanup.policy=delete, retention.ms=300000})
geojsonconverter-geojsonconverter-1  | 2023-11-09 23:10:32 [main] INFO  KafkaConfiguration - New Topic: (name=topic.ProcessedMapWKT, numPartitions=1, replicationFactor=1, replicasAssignments=null, configs={cleanup.policy=delete, retention.ms=300000})
```