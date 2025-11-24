package by.gsu.scherbak.webSocket;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import by.gsu.scherbak.webSocket.service.WebService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WebServiceTests {
    @InjectMocks
    private WebService webService;
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Test
    @Tag("sendOrder()")
    @DisplayName("sendOrder() should send an order to \"/topic/messages\"")
    void sendingOrderToWebSocket() {
        OrderEvent orderEvent = new OrderEvent();
        WebService spyService = spy(webService);

        webService.sendOrder(orderEvent);
        verify(messagingTemplate).convertAndSend("/topic/messages", orderEvent);

        spyService.consumeOrderEvent(orderEvent);
        verify(spyService).sendOrder(orderEvent);
    }
}
