package by.gsu.scherbak.orderService.controller;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import by.gsu.scherbak.orderService.repository.Order;
import by.gsu.scherbak.orderService.repository.interfaces.IOrderMapper;
import by.gsu.scherbak.orderService.service.KafkaProducerService;
import by.gsu.scherbak.orderService.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
 * Order controller class
 *
 * @version 1.1 30.09.2025
 * @author Scherbak Andrey
 * */
@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;
    private final KafkaProducerService kafkaProducerService;
    private final IOrderMapper iOrderMapper;

    public OrderController(OrderService orderService,
                           KafkaProducerService kafkaProducerService,
                           IOrderMapper iOrderMapper) {
        this.orderService = orderService;
        this.kafkaProducerService = kafkaProducerService;
        this.iOrderMapper = iOrderMapper;
    }

    /*POST method for getting orders*/
    @PostMapping
    public ResponseEntity<OrderEvent> gettingNewOrder(@RequestBody OrderEvent orderEvent)
            throws ExecutionException, InterruptedException {
        try {
            OrderEvent orderEventWithId = orderService.processingOrder(orderEvent);
            return ResponseEntity.ok(orderEventWithId);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to process order");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((OrderEvent) error);
        }
    }
}
