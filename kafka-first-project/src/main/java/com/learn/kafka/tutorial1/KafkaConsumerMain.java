package com.learn.kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerMain {

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumerMain.class);
    public static void main(String[] args) {
        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "my-fourth-application";
        String topicName = "first_topic";

        // create producer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG , groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
//        earliest [read from the beginning] /latest [only new messages are read] /none [when no offset is saved]

        //create consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);


        //subscriber consumer to topic
        consumer.subscribe(Arrays.asList(topicName));


        //poll new Data
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String,String> record : records)
            {
                logger.info("Key: {} , Value: {} ",record.key() ,record.value());
                logger.info("Partition: {} , Offset: {} ",record.partition() ,record.offset());
            }
        }

    }
}
