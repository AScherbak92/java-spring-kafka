package by.gsu.scherbak.orderService.service;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/*
 * Service for kafka producer
 *
 * @version 1.1 30.09.2025
 * @author Scherbak Andrey
 * */
@Service
@EnableRetry
public class KafkaProducerService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /*Method for sending message to topic "Orders-topic" in kafka*/
    @Retryable (
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendMessageToKafka(OrderEvent orderEvent)
            throws ExecutionException, InterruptedException {
        try {
            kafkaTemplate.send("Orders-topic",
                    orderEvent.getId(),toString(), orderEvent).get();

            LOGGER.info("Message {} sent successfully to kafka.", orderEvent.getId());

        } catch (Exception e) {
            LOGGER.error("Failed to send message to kafka: {}", orderEvent.getId(), e);
            throw e;
        }
    }

}
