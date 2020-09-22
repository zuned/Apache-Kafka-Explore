package com.learn.kafka.tutorial1;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerMain {
    public static void main(String[] args) {
        String bootstrapServers = "127.0.0.1:9092";
        // create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        // create producer
        org.apache.kafka.clients.producer.KafkaProducer<String , String> producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties);

        //create producer record
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "Hello Zuned Ahmed!");

        // send data
        producer.send(record);
        // flush the data
        producer.flush();
        // flush and close the stream
        producer.close();
    }
}
