package by.gsu.scherbak.orderService.kafka;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/*
 * Kafka configuration class
 *
 * @version 1.1 25.09.2025
 * @author Scherbak Andrey
 * */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.retries}")
    private String retries;

    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    private String deliveryTimeoutMs;

    /*Method for setting kafka producer configs*/
    Map<String, Object> setProducerConfigs() {
        Map<String, Object> configs = new HashMap<>();

        /*Producer properties*/
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        configs.put(ProducerConfig.ACKS_CONFIG, acks);
        configs.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs);
        configs.put(ProducerConfig.RETRIES_CONFIG, retries);

        return configs;
    }

    /*Bean for producer factory*/
    @Bean
    ProducerFactory<String, OrderEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(setProducerConfigs());
    }

    /*Bean for kafka template*/
    @Bean
    KafkaTemplate<String, OrderEvent> kafkaTemplate() {
        return new KafkaTemplate<String, OrderEvent>(producerFactory());
    }


    /*Bean for topic "Order-topic" in kafka*/
    @Bean
    NewTopic createOrderTopic() {
        return TopicBuilder.name("Orders-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
