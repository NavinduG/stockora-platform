package com.stockora.order.repository;

import com.stockora.order.model.Order;
import com.stockora.order.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class OrderRepositoryIT {

    @Container
    @ServiceConnection(name = "postgresql")
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void savedOrder_canBeFoundById() {
        Order order = new Order("SKU-IT-001", 5, "Integration Test Customer");
        order.setStatus(OrderStatus.CONFIRMED);

        Order saved = orderRepository.save(order);

        Optional<Order> found = orderRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getProductSku()).isEqualTo("SKU-IT-001");
        assertThat(found.get().getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    void findAll_returnsAllSavedOrders() {
        orderRepository.save(new Order("SKU-IT-002", 1, "Customer A"));
        orderRepository.save(new Order("SKU-IT-003", 2, "Customer B"));

        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSizeGreaterThanOrEqualTo(2);
    }
}