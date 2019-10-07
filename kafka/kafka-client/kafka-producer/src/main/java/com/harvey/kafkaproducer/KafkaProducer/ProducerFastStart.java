package com.harvey.kafkaproducer.KafkaProducer;


import com.harvey.kafkaproducer.Interceptor.ProducerInterceptorPrefix;
import com.harvey.kafkaproducer.Partitioner.DemoPartitioner;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

public class ProducerFastStart {
    public static final String brokerList = "192.168.6.122:9092";
    public static final String topic = "Hello-Kafka2";

    public static void sendMessage(String strMessage) {
        Properties properties = new Properties();

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DemoPartitioner.class.getName());

        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, ProducerInterceptorPrefix.class.getName());

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);

        //thread safe
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //must have topic and value
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, strMessage);

        try {
            // fire-and-forget
            //producer.send(record);

            //sync
            //producer.send(record).get();

            //or
           /* Future<RecordMetadata> future = producer.send(record);
            RecordMetadata metadata = future.get();
            System.out.println(metadata.topic() + "-" + metadata.partition() + ":" + metadata.offset());*/

            //async
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        System.out.println(metadata.topic() + "-" + metadata.partition() + ":" + metadata.offset());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        producer.close();
    }
}
