package com.example.user_service;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic userCreatedTopic() {
        return TopicBuilder.name("user-created-events-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic userDeletedTopic() {
        return TopicBuilder.name("user-deleted-events-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
