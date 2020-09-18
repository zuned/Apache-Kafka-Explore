# kafka
## kafka CLI
- Create Topic : > `kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 --replication-factor 1`
- View All Topic that are created : > `kafka-topics --zookeeper 127.0.0.1:2181 --list`
- Describe Specific topic : > `kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --describe`
- Delete a Topic : > `kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --delete`
- How to use console producer : > `kafka-console-producer --broker-list 127.0.0.1:9092 --topic first_topic`
- How to use console producer with property ack : > `kafka-console-producer --broker-list 127.0.0.1:9092 --topic first_topic --producer-property acks=all`
