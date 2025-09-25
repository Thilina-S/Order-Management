package com.example.order.kafka;


import com.example.base.dto.GoodEventDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class GoodProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodEventDTO.class);

    private final NewTopic goodTopic;
    private final KafkaTemplate <String, GoodEventDTO> kafkaTemplate;

    public GoodProducer(NewTopic goodTopic, KafkaTemplate<String, GoodEventDTO> kafkaTemplate) {
        this.goodTopic = goodTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(GoodEventDTO goodEventDTO){
        LOGGER.info(String.format("Sending order event to topid %s", goodEventDTO.toString()));

        Message<GoodEventDTO> message = MessageBuilder
                .withPayload(goodEventDTO)
                .setHeader(KafkaHeaders.TOPIC, goodTopic.name())
                .build();

        kafkaTemplate.send(message);
    }

}
