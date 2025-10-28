package by.gsu.scherbak.webSocket.controller;

import by.gsu.scherbak.webSocket.service.WebService;
import by.gsu.scherbak.webSocket.test.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/*
* Controller for websockets
*
* */
@Controller
public class WebController {

    private final WebService webService;

    public WebController(WebService webService) {
        this.webService = webService;
    }


    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message getMessage(Message message) {
        System.out.println(message.getMessage());
        return message;
    }
}
