cd ~/kafka/
KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"

bin/kafka-storage.sh format -t $KAFKA_CLUSTER_ID -c config/kraft/server.properties

# start kafka
bin/kafka-server-start.sh -daemon config/kraft/server.properties
# wait 2 seconds for the server to start and be able to add partitions
sleep 2s
# add topics

# BSM
bin/kafka-topics.sh --create --topic "topic.OdeBsmJson" --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
bin/kafka-topics.sh --create --topic "topic.ProcessedBsm" --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1

# MAP
bin/kafka-topics.sh --create --topic "topic.OdeMapJson" --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
bin/kafka-topics.sh --create --topic "topic.ProcessedMap" --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1

# SPaT
bin/kafka-topics.sh --create --topic "topic.OdeSpatJson" --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
bin/kafka-topics.sh --create --topic "topic.ProcessedSpat" --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
