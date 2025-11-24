package by.gsu.scherbak.orderService;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import by.gsu.scherbak.orderService.service.KafkaProducerService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
* TESTS FOR KafkaProducerService methods
* */
@ExtendWith(MockitoExtension.class)
public class KafkaProducerServiceTests {
    @Mock
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;
    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    //Correct OrderEvent
    private OrderEvent successEvent;
    //Artificially incorrect OrderEvent
    private OrderEvent dltEvent;

    /*Setup for initializing correct OrderEvent and incorrect OrderEvent*/
    @BeforeEach
    void setup(){
        successEvent = new OrderEvent();
        dltEvent = new OrderEvent();

        successEvent.setId(1);
        successEvent.setPrice(1000);
        successEvent.setDescription("Correct order");

        dltEvent.setId(2);
        dltEvent.setPrice(2000);
        dltEvent.setDescription("DLT");
    }

    /*Test for method sendMessageToKafka()
    * Assuming successful sending correct OrderEvent message by using sendMessageToKafka(),
    *   and not sending anything to DLT*/
    @Test
    @Tag("sendMessageToKafka()")
    @DisplayName("sendMessageToKafka() method sending correct message")
    void sendCorrectMessageToKafkaProducer() throws ExecutionException, InterruptedException {
        when(kafkaTemplate.executeInTransaction(any())).thenReturn(true);

        kafkaProducerService.sendMessageToKafka(successEvent);

        //Verifying we send message only once
        verify(kafkaTemplate, times(1)).executeInTransaction(any());
        //Verifying we never send anything to DLT
        verify(kafkaTemplate, never()).send(eq("Orders-topic.DLT"), anyString(), any());
    }

    /*Test for method sendMessageToKafka()
     * Assuming that sendMessageToKafka() should send dltEvent message to DLT topic*/
    @Test
    @Tag("sendMessageToKafka()")
    @DisplayName("sendMessageToKafka() should send dltEvent to DLT topic")
    void sendDltEventToDltTopic(){
        assertThrows(RuntimeException.class, () -> {
            kafkaProducerService.sendMessageToKafka(dltEvent);
        });

        //Verifying we send our message to DLT exactly one time
        verify(kafkaTemplate, times(1)).send(
                eq("Orders-topic.DLT"),
                eq(dltEvent.getId().toString()),
                eq(dltEvent)
        );

        //Verifying there was no other interactions with KafkaTemplate
        verify(kafkaTemplate, never()).executeInTransaction(any());
    }

    /*Test for method sendToDLT()
     * Scenario when sending to DLT also fails
     * In this scenario we must catch an exception and gracefully handle it,
     *   without crashing whole application */
    @Test
    @Tag("sendToDLT()")
    @DisplayName("sendToDLT() should fail to send message even to DLT topic")
    void failToSendEvenToDltTopic() {
        //Simulating DLT failure by throwing exception in .send() method
        when(kafkaTemplate.send(anyString(), anyString(), any(OrderEvent.class)))
                .thenThrow(new RuntimeException("Artificial exception when send to DLT"));

        //Calling sendToDLT() method with exception should not crash application
        kafkaProducerService.sendToDLT(dltEvent, new RuntimeException("Artificial exception"));

        //Verifying that we attempted to send message and getting an exception
        verify(kafkaTemplate, times(1)).send(
                eq("Orders-topic.DLT"),
                eq(dltEvent.getId().toString()),
                eq(dltEvent)
        );

        //try-catch block handled an exception gracefully and application works well

        verifyNoMoreInteractions(kafkaTemplate);
    }
}
