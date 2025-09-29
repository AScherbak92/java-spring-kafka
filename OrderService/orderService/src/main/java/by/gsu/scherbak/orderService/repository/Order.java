package by.gsu.scherbak.orderService.repository;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import jakarta.persistence.*;

import java.time.LocalDate;


/*
* Order entity class for table "orders" in relational DB.
* Extends fields form OrderEvent
*
* @version 1.0 21.09.2025
* @author Scherbak Andrey
* */
@Entity
@Table(name="orders")
public class Order extends OrderEvent {
    public Order() {
        super();
    }

    public Order(OrderEvent orderEvent) {
        this.setDescription(orderEvent.getDescription());
        this.setOrderTimestamp(orderEvent.getOrderTimestamp());
        this.setPrice(orderEvent.getPrice());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setId(Integer id) {
        super.setId(id);
    }

    @Override
    public Integer getPrice() {
        return super.getPrice();
    }

    @Override
    public void setPrice(Integer price) {
        super.setPrice(price);
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Override
    public LocalDate getOrderTimestamp() {
        return super.getOrderTimestamp();
    }

    @Override
    public void setOrderTimestamp(LocalDate orderTimestamp) {
        super.setOrderTimestamp(orderTimestamp);
    }

}
