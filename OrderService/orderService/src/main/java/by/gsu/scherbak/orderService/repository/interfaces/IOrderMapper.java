package by.gsu.scherbak.orderService.repository.interfaces;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import by.gsu.scherbak.orderService.repository.Order;
import org.mapstruct.Mapper;

/*
 * Mapper interface for Order and OrderEvent
 *
 * @version 1.0 30.09.2025
 * @author Scherbak Andrey
 * */
@Mapper(componentModel = "spring")
public interface IOrderMapper {
    /*Mapping from OrderEvent to Order*/
    Order toEntity(OrderEvent orderEvent);

    /*Mapping from Order to OrderEvent*/
    OrderEvent toEvent(Order order);
}
