package by.gsu.scherbak.orderService.service;

import by.gsu.scherbak.orderService.repository.Order;
import by.gsu.scherbak.orderService.repository.OrderRepositoryInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/*
 * Order service class
 *
 * @version 1.0 21.09.2025
 * @author Scherbak Andrey
 * */
@Service
public class OrderService {
    private final OrderRepositoryInterface orderRepositoryInterface;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public OrderService(OrderRepositoryInterface orderRepositoryInterface) {
        this.orderRepositoryInterface = orderRepositoryInterface;
    }

    /*Saving order entity to relational database*/
    public boolean saveOrder(Order order){

        /*Checking if order with current id exists*/
        if (order.getId() != null && orderRepositoryInterface.findById(order.getId()).isPresent()) {
            throw new RuntimeException("Order with this id already exists in DB");
        }

        orderRepositoryInterface.save(order);

        LOGGER.info("Order {} was succesfully saved to relational DB", order.getId());

        return true;
    }

    /*Deleting order from relational database*/
    public void deleteOrder(Integer id){
        orderRepositoryInterface.deleteById(id);
    }
}
