package com.atlas.inventory.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic stockReservedTopic() {
        return TopicBuilder.name("atlas.stock.reserved").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic stockRejectedTopic() {
        return TopicBuilder.name("atlas.stock.rejected").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic orderCreatedDltTopic() {
        return TopicBuilder.name("atlas.order.created.DLT").partitions(3).replicas(1).build();
    }
}
