package com.atlas.auth;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class InfrastructureContainersIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("atlas_test")
            .withUsername("atlas")
            .withPassword("atlas");

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");

    @Container
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    @Test
    void shouldStartPostgresMongoAndKafkaContainers() {
        assertTrue(postgres.isRunning());
        assertNotNull(postgres.getJdbcUrl());

        assertTrue(mongo.isRunning());
        assertNotNull(mongo.getReplicaSetUrl());

        assertTrue(kafka.isRunning());
        assertNotNull(kafka.getBootstrapServers());
    }
}
