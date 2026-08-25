package com.stockora.order.service;

import com.stockora.order.client.InventoryClient;
import com.stockora.order.client.ProductResponse;
import com.stockora.order.model.Order;
import com.stockora.order.model.OrderStatus;
import com.stockora.order.repository.OrderRepository;
import com.stockora.order.event.OrderEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @Test
    void createOrder_whenEnoughStock_confirmsOrder() {
        ProductResponse product = new ProductResponse();
        product.setSku("SKU-001");
        product.setQuantity(10);

        Order order = new Order("SKU-001", 3, "John Doe");

        when(inventoryClient.getProductBySku("SKU-001")).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.createOrder(order);

        assertThat(result.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    void createOrder_whenNotEnoughStock_rejectsOrder() {
        ProductResponse product = new ProductResponse();
        product.setSku("SKU-002");
        product.setQuantity(1);

        Order order = new Order("SKU-002", 5, "Jane Smith");

        when(inventoryClient.getProductBySku("SKU-002")).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.createOrder(order);

        assertThat(result.getStatus()).isEqualTo(OrderStatus.REJECTED);
    }

    @Test
    void createOrder_whenProductNotFound_rejectsOrder() {
        Order order = new Order("SKU-FAKE", 1, "Test User");

        when(inventoryClient.getProductBySku("SKU-FAKE")).thenReturn(Optional.empty());
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.createOrder(order);

        assertThat(result.getStatus()).isEqualTo(OrderStatus.REJECTED);
    }
}