package by.gsu.scherbak.ListenerMicorservice;

import by.gsu.scherbak.ListenerMicorservice.consumer.KafkaConsumer;
import by.gsu.scherbak.orderLibrary.OrderEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class KafkaConsumerTests {
    @InjectMocks
    private KafkaConsumer kafkaConsumer;
    @Mock
    private Logger logger;

    OrderEvent orderEvent;

    @BeforeEach
    void setup(){
        ReflectionTestUtils.setField(kafkaConsumer, "LOGGER", logger);
        orderEvent = new OrderEvent();
    }

    /*Test for method processKafkaMessage()
     * Assuming successful processing correct OrderEvent message from kafka*/
    @Test
    @Tag("processKafkaMessage()")
    @DisplayName("processKafkaMessage() should successfully process correct OrderEvent")
    void processingValidKafkaMessage() {
        orderEvent.setId(1);

        kafkaConsumer.processKafkaMessage(orderEvent);

        verify(logger).info(contains("New order was created:"));
    }

    /*Test for method processKafkaMessage()
     * Assuming logging IllegalArgumentException when id=null*/
    @Test
    @Tag("processKafkaMessage()")
    @DisplayName("processKafkaMessage() should log an error if IllegalArgumentException")
    void processingInvalidKafkaMessage() {
        orderEvent.setId(null);

        kafkaConsumer.processKafkaMessage(orderEvent);

        verify(logger).error("Validation order. Order ID cannot be null.: {}.", orderEvent.toString());
    }

    /*Test for method processDLTMessage()
     * Assuming successful processing message from DLT*/
    @Test
    @Tag("processDLTMessage()")
    @DisplayName("processDLTMessage() should log a warn about new DLT message")
    void processingReceinvgMessageFromDLT() {
        orderEvent.setId(1);

        kafkaConsumer.processDLTMessage(orderEvent);

        verify(logger).warn("Received message in DLT: {}", orderEvent);
    }
}
