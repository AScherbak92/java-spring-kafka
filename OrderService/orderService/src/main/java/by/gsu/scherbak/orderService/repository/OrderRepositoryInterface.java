package by.gsu.scherbak.orderService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * JPA repository for Order class
 *
 * @version 1.0 21.09.2025
 * @author Scherbak Andrey
 * */
public interface OrderRepositoryInterface extends JpaRepository<Order, Integer> {
    Optional<Order> findById(Integer integer);
}
