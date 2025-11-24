package by.gsu.scherbak.webSocket.service;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/*
* Service for websockets
*
* */
@Service
public class WebService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /*Method for sending message to web*/
    public void sendOrder(OrderEvent orderEvent){
        messagingTemplate.convertAndSend("/topic/messages", orderEvent);
    }

    /*Listening to orders-topic in kafka*/
    @KafkaListener(topics="Orders-topic")
    public void consumeOrderEvent(OrderEvent orderEvent){
        /*Sending message to web*/
        sendOrder(orderEvent);
    }
}
