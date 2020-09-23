# kafka

### To Run Kafka And Zookeeper we need to go in kafka_home directory
- Start Zookeeper : `zookeeper-server-start config/zookeeper.properites`
- Start Kafka : `kafka-server-start config/server.properties`

## kafka CLI
- Create Topic : > `kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 --replication-factor 1`
- View All Topic that are created : > `kafka-topics --zookeeper 127.0.0.1:2181 --list`
- Describe Specific topic : > `kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --describe`
- Delete a Topic : > `kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --delete`

- How to use console producer : > `kafka-console-producer --broker-list 127.0.0.1:9092 --topic first_topic`
- How to use console producer with property ack : > `kafka-console-producer --broker-list 127.0.0.1:9092 --topic first_topic --producer-property acks=all`

- default topic setting can be updated in server.properties , If topic name provided in producer which does not exist , Kafka will create one with default settings

- Console Consumer reading from beginning : > `kafka-console-consumer --bootstrap-server 127.0.0.1:9092  --topic first_topic --from-beginning`
- if beginning is not provided console consumer will wait for new message : > `kafka-console-consumer --bootstrap-server 127.0.0.1:9092  --topic first_topic `
- Consumer with same group will disrtibute data among them selves : >  `kafka-console-consumer --bootstrap-server 127.0.0.1:9092  --topic first_topic --group my-first-application`
- List all consumber group : > `kafka-consumer-groups --bootstrap-server 127.0.0.1:9092 --list' Please if you not specify consumer group kafka will generate its own for each consumer
- descibe consumber group : > `kafka-consumer-groups --bootstrap-server 127.0.0.1:9092 --describe --group my-first-application`
- reset offset of consumer group : > `kafka-consumer-groups --bootstrap-server 127.0.0.1:9092 --group my-first-application --reset-offsets --to-earliest --execute --topic first_topic`

## Kafka Properties

-- Idempotent
-- High Throughput
-- Safe Producer
-- Producer Batching

