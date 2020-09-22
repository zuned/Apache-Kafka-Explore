package com.learn.kafka.tutorial1;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaProducerWithCallback {
    private static Logger logger = LoggerFactory.getLogger(KafkaProducerWithCallback.class);

    public static void main(String[] args) {
        String bootstrapServers = "127.0.0.1:9092";
        // create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        // create producer
        KafkaProducer<String , String> producer = new KafkaProducer<String, String>(properties);
        for(int i =0 ; i < 10 ; i++) {
            //create producer record
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "Hello Zuned Ahmed! " + i);

            // send data
            producer.send(record,(recordMetadata, e) -> {
                //this is onCompletion method called every time when record is successfully sent or exception is thrown
                if(e==null){
                    logger.info("Received new metadata.");
                    logger.info("Topic: {} ", recordMetadata.topic() );
                    logger.info("Partition: {} ", recordMetadata.partition() );
                    logger.info("Offset: {} ", recordMetadata.offset() );
                    logger.info("Timestamp: {} ", recordMetadata.timestamp() );
                }else {
                    logger.error("Error while producing ",e);
                }
            });
        }

        // flush the data
        producer.flush();
        // flush and close the stream
        producer.close();
    }
}
