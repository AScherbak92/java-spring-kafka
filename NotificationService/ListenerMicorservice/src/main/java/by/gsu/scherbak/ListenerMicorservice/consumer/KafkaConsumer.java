package by.gsu.scherbak.ListenerMicorservice.consumer;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/*
 * Service for kafka consumer
 *
 * @version 1.1 30.09.2025
 * @author Scherbak Andrey
 * */
@Service
public class KafkaConsumer {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /*Method for processing kafka message from "Orders-topic" topic*/
    @KafkaListener(
            topics="${kafka.consumer.topics}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void processKafkaMessage(OrderEvent order) {
        try {
            LOGGER.info("New order was created: " + order.toString());

            /*Null check*/
            if (order.getId() == null) {
                throw new IllegalArgumentException("Order ID is null.");
            }

        } catch (IllegalArgumentException e) {
            LOGGER.error("Validation order. Order ID cannot be null.: {}.", order.toString());
        } catch (Exception e) {
            LOGGER.error("Unexpected processing order: {}. Error: {}", order.toString(), e.getMessage());
        }
    }
}
