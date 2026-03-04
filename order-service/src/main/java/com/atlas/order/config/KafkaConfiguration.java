package com.atlas.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic orderCreatedTopic() {
        return TopicBuilder.name("atlas.order.created").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic orderCompletedTopic() {
        return TopicBuilder.name("atlas.order.completed").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic orderCancelledTopic() {
        return TopicBuilder.name("atlas.order.cancelled").partitions(3).replicas(1).build();
    }
}
