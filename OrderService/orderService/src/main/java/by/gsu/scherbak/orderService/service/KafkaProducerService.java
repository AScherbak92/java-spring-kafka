package by.gsu.scherbak.orderService.service;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import by.gsu.scherbak.orderService.repository.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
 * Service for kafka producer
 *
 * @version 1.0 21.09.2025
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
    @Async
    public CompletableFuture<Boolean> sendMessageToKafka(OrderEvent orderEvent)
            throws ExecutionException, InterruptedException {

        return kafkaTemplate.send("Orders-topic", orderEvent)
                .toCompletableFuture()
                .thenApply(result -> {
                    LOGGER.info("Message sent successfully");
                    return true;
                })
                .exceptionally(throwable -> {
                    LOGGER.info("Failed to send message to kafka: {}", orderEvent.getId(), throwable);
                    return false;
                });
    }

}
