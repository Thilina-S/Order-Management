package com.example.order.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

//kafka topic configurations
@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.template.default-topic}")  //getting the topic define in the application.properties file using bean
    private String topicName;

    @Bean
    public NewTopic createTopic(){
        return TopicBuilder.name(topicName).partitions(1).replicas(1).build();
    }

}
