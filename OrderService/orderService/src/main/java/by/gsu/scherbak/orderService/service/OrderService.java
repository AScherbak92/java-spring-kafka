package by.gsu.scherbak.orderService.service;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import by.gsu.scherbak.orderService.repository.Order;
import by.gsu.scherbak.orderService.repository.interfaces.IOrderMapper;
import by.gsu.scherbak.orderService.repository.interfaces.OrderRepositoryInterface;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/*
 * Order service class
 *
 * @version 1.1 30.09.2025
 * @author Scherbak Andrey
 * */
@Service
public class OrderService {
    private final OrderRepositoryInterface orderRepositoryInterface;
    private final IOrderMapper iOrderMapper;
    private final KafkaProducerService kafkaProducerService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public OrderService(OrderRepositoryInterface orderRepositoryInterface,
                        IOrderMapper iOrderMapper,
                        KafkaProducerService kafkaProducerService) {
        this.orderRepositoryInterface = orderRepositoryInterface;
        this.iOrderMapper = iOrderMapper;
        this.kafkaProducerService = kafkaProducerService;
    }

    /*Saving order entity to relational database*/
    public void saveOrderToBD(Order order){

        /*Checking if order with current id exists*/
        if (order.getId() != null && orderRepositoryInterface.findById(order.getId()).isPresent()) {
            throw new RuntimeException("Order with this id already exists in DB");
        }

        orderRepositoryInterface.save(order);

        LOGGER.info("Order {} was succesfully saved to relational DB", order.getId());
    }

    /*Deleting order from relational database*/
    public void deleteOrder(Integer id){
        orderRepositoryInterface.deleteById(id);
    }

    /*Processing order and sending to DB and Kafka
    * (By default, OrderEvent id is null!!)*/
    @Transactional
    public OrderEvent processingOrder(OrderEvent orderEvent)
            throws ExecutionException, InterruptedException {
        OrderEvent orderEventWithId;
        Order order;

        /*Mapping from OrderEvent to Order with generated id*/
        order = iOrderMapper.toEntity(orderEvent);

        /*Saving order entity to relational database*/
        saveOrderToBD(order);

        /*Mapping from Order to OrderEvent with id*/
        orderEventWithId = iOrderMapper.toEvent(order);

        /*Sending OrderEvent with id to kafka*/
        kafkaProducerService.sendMessageToKafka(orderEventWithId);

        return orderEventWithId;
    }
}
