package com.learn.kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerAssignAndSeekDemo {

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumerAssignAndSeekDemo.class);
    public static void main(String[] args) {
        String bootstrapServers = "127.0.0.1:9092";
//        String groupId = "my-fourth-application";
        String topicName = "first_topic";

        // create producer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
//        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG , groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
//        earliest [read from the beginning] /latest [only new messages are read] /none [when no offset is saved]

        //create consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        //assign and seek are used mostly to replay data or fetch specific Message
        TopicPartition partitionReadFrom = new TopicPartition(topicName , 0);
        long offsetToReadFrom = 15L;
        consumer.assign(Arrays.asList(partitionReadFrom));

        consumer.seek(partitionReadFrom , offsetToReadFrom);
        int numberOfMessageToRead = 5;
        boolean keepOnReading =true;
        int numberOfMessageRead = 0;
        //poll new Data
        while (keepOnReading) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String,String> record : records)
            {
                ++numberOfMessageRead;
                logger.info("Key: {} , Value: {} ",record.key() ,record.value());
                logger.info("Partition: {} , Offset: {} ",record.partition() ,record.offset());
                if(numberOfMessageRead >= numberOfMessageToRead)
                    keepOnReading = false;
                break;
            }
        }
        logger.info("We are exiting application.");
    }
}
