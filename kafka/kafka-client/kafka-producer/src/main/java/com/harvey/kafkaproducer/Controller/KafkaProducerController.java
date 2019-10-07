package com.harvey.kafkaproducer.Controller;

import com.harvey.kafkaproducer.KafkaProducer.ProducerFastStart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducerController {

    @GetMapping("/message")
    boolean sendKafkaMessage(@RequestParam String message) {
        ProducerFastStart.sendMessage(message);
        return true;
    }
}
