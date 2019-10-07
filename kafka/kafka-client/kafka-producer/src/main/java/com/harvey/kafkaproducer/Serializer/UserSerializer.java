package com.harvey.kafkaproducer.Serializer;

import com.harvey.kafkaproducer.Model.User;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserSerializer implements Serializer<User> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, User user) {
        return new byte[0];
    }

    @Override
    public void close() {

    }
}
