package by.gsu.scherbak.ListenerMicorservice.consumer;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/*
 * Service for kafka consumer
 *
 * @version 1.0 24.09.2025
 * @author Scherbak Andrey
 * */
@Service
public class KafkaConsumer {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /*Kafka listener of topic "Orders-topic"*/
    @KafkaListener(topics = "Orders-topic", groupId = "Orders-topic")
    public OrderEvent kafkaListener(OrderEvent order) {
        LOGGER.info("New order was created: " + order.toString());

        return order;
    }
}
