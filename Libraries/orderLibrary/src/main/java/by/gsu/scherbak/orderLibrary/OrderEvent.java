package by.gsu.scherbak.orderLibrary;

import java.time.LocalDate;

/*
* Order entity class for table "orders" in relational DB
*
* @version 1.0 21.09.2025
* @author Scherbak Andrey
* */
public class OrderEvent {
    private Integer id;
    private Integer price;
    private String description;
    private LocalDate orderTimestamp;

    public OrderEvent() {
        this.orderTimestamp = LocalDate.now();
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(LocalDate orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "id=" + id +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", orderTimestamp=" + orderTimestamp +
                '}';
    }
}
