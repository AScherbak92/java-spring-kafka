package by.gsu.scherbak.orderService;

import by.gsu.scherbak.orderLibrary.OrderEvent;
import by.gsu.scherbak.orderService.repository.Order;
import by.gsu.scherbak.orderService.repository.interfaces.IOrderMapper;
import by.gsu.scherbak.orderService.repository.interfaces.OrderRepositoryInterface;
import by.gsu.scherbak.orderService.service.KafkaProducerService;
import by.gsu.scherbak.orderService.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/*
 * TESTS FOR Order Service methods
 * */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {
    @Mock
    private KafkaProducerService kafkaProducerService;
    @Mock
    private OrderRepositoryInterface orderRepositoryInterface;
    @Mock
    private IOrderMapper iOrderMapper;
    @InjectMocks
    private OrderService orderService;
    /*Test for method saveOrderToBD()
     * Assuming successful saving order to DB*/
    @Test
    @Tag("saveOrderToBD()")
    @DisplayName("saveOrderToBD() successfully saving order to DB")
    void saveNewOrderToDB(){
        Order order = new Order();
        order.setId(1);

        //Imitated searching for order with id==1
        when(orderRepositoryInterface.findById(1)).thenReturn(Optional.empty());
        when(orderRepositoryInterface.save(order)).thenReturn(order);

        orderService.saveOrderToBD(order);

        //Verifying we did try to search order with id==1
        verify(orderRepositoryInterface).findById(1);
        //Verifying we saved the order to DB
        verify(orderRepositoryInterface).save(order);
    }

    /*Test for method saveOrderToBD()
     * Assuming this method should throw an exception,
     *   when try to save order with 'id' that already exists in DB*/
    @Test
    @Tag("saveOrderToBD()")
    @DisplayName("saveOrderToBD() should not save order with already existed id in DB")
    void saveNewOrderToDBShouldFail() {
        Order existingOrder = new Order();
        Order orderWithSameId = new Order();
        existingOrder.setId(1);
        orderWithSameId.setId(1);

        //Imitating that we've found order with same id in DB
        when(orderRepositoryInterface.findById(1)).thenReturn(Optional.of(existingOrder));

        //Asserting that we got an exception
        assertThrows(RuntimeException.class, () -> {
            orderService.saveOrderToBD(orderWithSameId);
        });

        //Verifying we did try to find an order with id=1
        verify(orderRepositoryInterface).findById(1);
        //Verifying we didn't save anything to DB
        verify(orderRepositoryInterface, never()).save(any());
    }

    @Test
    @Tag("processingOrder()")
    @DisplayName("Exception in processingOrder() should cause rollback of transaction")
    void processingOrderRollbackTransaction() throws ExecutionException, InterruptedException {
        OrderEvent inputEvent = new OrderEvent();
        OrderEvent outputEvent = new OrderEvent();
        Order orderEntity = new Order();

        outputEvent.setId(1);

        when(iOrderMapper.toEntity(inputEvent)).thenReturn(orderEntity);
        when(iOrderMapper.toEvent(orderEntity)).thenReturn(outputEvent);

        //Throwing an exception during sendMessageToKafka() method
        doThrow(new RuntimeException("Kafka error")).
                when(kafkaProducerService).sendMessageToKafka(outputEvent);

        //Asserting we have an exception in processingOrder() method
        assertThrows(RuntimeException.class, () -> {
            orderService.processingOrder(inputEvent);
        });

        //Making sure that we first tried to save in DB, and only then to send order to kafka
        InOrder inOrder = inOrder(orderRepositoryInterface, kafkaProducerService);
        inOrder.verify(orderRepositoryInterface).save(orderEntity);
        inOrder.verify(kafkaProducerService).sendMessageToKafka(outputEvent);

        //Verifying that rollback automatically  happened
        verifyNoMoreInteractions(orderRepositoryInterface);
    }
}
