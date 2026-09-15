package com.stockora.inventory.listener;

import com.stockora.inventory.event.OrderCreatedEvent;
import com.stockora.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderCreatedListenerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderCreatedListener listener;

    @Test
    void handleOrderCreated_whenConfirmed_decrementsStock() {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(1L);
        event.setProductSku("SKU-001");
        event.setQuantity(2);
        event.setStatus("CONFIRMED");

        when(productRepository.decrementStock("SKU-001", 2)).thenReturn(1);

        listener.handleOrderCreated(event);

        verify(productRepository, times(1)).decrementStock("SKU-001", 2);
    }

    @Test
    void handleOrderCreated_whenRejected_doesNotTouchStock() {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(2L);
        event.setProductSku("SKU-002");
        event.setQuantity(5);
        event.setStatus("REJECTED");

        listener.handleOrderCreated(event);

        verify(productRepository, never()).decrementStock(anyString(), anyInt());
    }

    @Test
    void handleOrderCreated_whenDecrementFails_doesNotThrow() {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(3L);
        event.setProductSku("SKU-003");
        event.setQuantity(10);
        event.setStatus("CONFIRMED");

        when(productRepository.decrementStock("SKU-003", 10)).thenReturn(0);

        listener.handleOrderCreated(event);

        verify(productRepository, times(1)).decrementStock("SKU-003", 10);
    }
}