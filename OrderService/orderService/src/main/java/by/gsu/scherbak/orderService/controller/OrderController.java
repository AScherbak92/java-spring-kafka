package by.gsu.scherbak.orderService.controller;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import by.gsu.scherbak.orderService.repository.Order;
import by.gsu.scherbak.orderService.service.KafkaProducerService;
import by.gsu.scherbak.orderService.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
 * Order controller class
 *
 * @version 1.0 21.09.2025
 * @author Scherbak Andrey
 * */
@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;
    private final KafkaProducerService kafkaProducerService;

    public OrderController(OrderService orderService, KafkaProducerService kafkaProducerService) {
        this.orderService = orderService;
        this.kafkaProducerService = kafkaProducerService;
    }

    /*POST method for sending orders entity*/
    @PostMapping
    public CompletableFuture<OrderEvent> sendOrder(@RequestBody OrderEvent orderEvent)
            throws ExecutionException, InterruptedException {
        /*Saving order entity to relational database*/
        Order order = new Order(orderEvent);
        orderService.saveOrder(order);

        /*Setting id for OrderEvent entity from generated @id field in Order*/
        orderEvent.setId(order.getId());

        return kafkaProducerService.sendMessageToKafka(orderEvent)
                .thenCompose(success -> {
                    if (success) {
                        return CompletableFuture.completedFuture(orderEvent);
                    } else {
                        orderService.deleteOrder(order.getId());

                        return CompletableFuture.failedFuture(
                                new RuntimeException("Failed to send message to kafka." +
                                        "Deleting order from DB")
                        );
                    }
                }).exceptionally(throwable -> {
                    orderService.deleteOrder(order.getId());

                    throw new RuntimeException("Order processing failed. Order deleted from DB");
                });
    }
}
