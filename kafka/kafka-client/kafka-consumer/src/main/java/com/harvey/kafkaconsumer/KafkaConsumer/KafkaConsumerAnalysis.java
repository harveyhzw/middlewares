package com.harvey.kafkaconsumer.KafkaConsumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class KafkaConsumerAnalysis {
    public static final String brokerList = "192.168.6.122:9092";
    public static final String topic = "Hello-Kafka2";
    public static final String groupId = "group.hello";
    public static final String clientId = "client.id.demo";

    public static final AtomicBoolean isRunning = new AtomicBoolean(true);

    public static Properties initConfig() {
        Properties props = new Properties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        return props;
    }

    public static void main(String[] args) {
        Properties properties = initConfig();

        // not thread safe
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        //subscribe partitions
        List<TopicPartition> partitions = new ArrayList<>();
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        if (partitionInfos != null){
            for(PartitionInfo pInfo : partitionInfos){
                partitions.add(new TopicPartition(pInfo.topic(), pInfo.partition()));
            }
        }
        consumer.assign(partitions);

        //re-balance

        /*consumer.subscribe(Arrays.asList(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                consumer.commitSync();

            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

            }
        });*/


        //consumer.subscribe(Arrays.asList(topic));

        //consumer.subscribe(Pattern.compile("topic-.*"));

        //consumer.unsubscribe();

        try {

            while (isRunning.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("topic = " + record.topic()
                            + ", partition = " + record.partition()
                            + ", offset = " + record.offset());

                    System.out.println("key = " + record.key()
                            + ", value = " + record.value());

                    // to do

                }

                consumer.commitSync();
                //consumer.seek(paration, 10);
                //consumer.seekToBeginning();
                //consumer.seekToEnd();
                //consumer.offsetsForTimes()
            }

        } catch (Exception e) {
            System.out.println("occurred exception:" + e);
        } finally {
            consumer.close();
        }

    }

}
