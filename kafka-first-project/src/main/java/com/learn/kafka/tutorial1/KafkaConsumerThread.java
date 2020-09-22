package com.learn.kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class KafkaConsumerThread {

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumerThread.class);
    public static void main(String[] args) {
        new KafkaConsumerThread().callConsumer();
    }

    private void callConsumer() {
        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "my-fifth-application";
        String topicName = "first_topic";
        CountDownLatch latch = new CountDownLatch(1);
        Runnable myConsumerRunnable = new ConsumerRunnable(bootstrapServers,groupId,topicName,latch);
        Thread myThread = new Thread(myConsumerRunnable);
        myThread.start();

        //Add shutDown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            logger.info("Caught shutdown hook.");
            ((ConsumerRunnable)myConsumerRunnable).shutdown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.info("Latch interrupt inside shutdown hook !");
            }
            logger.info("Application is exited.");
        }));

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.info("Application got interrupt !");
        }finally {
            logger.info("Application is closing.");
        }
    }

    class ConsumerRunnable implements  Runnable {
        private Logger logger = LoggerFactory.getLogger(ConsumerRunnable.class);
        private CountDownLatch latch;
        private KafkaConsumer<String,String> consumer;

        ConsumerRunnable(String bootstrapServers,
                         String groupId,
                         String topicName,
                         CountDownLatch latch ) {
            this.latch = latch;
            // create producer properties
            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG , groupId);
//        earliest [read from the beginning] /latest [only new messages are read] /none [when no offset is saved]
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

            //create consumer
            this.consumer = new KafkaConsumer<String, String>(properties);
            //subscriber consumer to topic
            consumer.subscribe(Arrays.asList(topicName));

        }

        @Override
        public void run() {
            try{
                //poll new Data
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for(ConsumerRecord<String,String> record : records)
                    {
                        logger.info("Key: {} , Value: {} ",record.key() ,record.value());
                        logger.info("Partition: {} , Offset: {} ",record.partition() ,record.offset());
                    }
                }
            }catch(WakeupException wakeUp){
                logger.info("Received request to shutdown signal!");
            } finally {
                consumer.close();
                latch.countDown();
            }
        }

        public void shutdown() {
            //this special method to interrupt consumer.poll()
            // it will make it come up with exception . It will throw WakeUpException.
            this.consumer.wakeup();
        }
    }
}

