package com.stockora.order.service;

import com.stockora.order.client.InventoryClient;
import com.stockora.order.client.ProductResponse;
import com.stockora.order.event.OrderEventPublisher;
import com.stockora.order.model.Order;
import com.stockora.order.model.OrderStatus;
import com.stockora.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final OrderEventPublisher orderEventPublisher;

    public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient, OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.orderEventPublisher = orderEventPublisher;
    }

    public Order createOrder(Order order) {
        Optional<ProductResponse> productOpt = inventoryClient.getProductBySku(order.getProductSku());

        if (productOpt.isEmpty()) {
            order.setStatus(OrderStatus.REJECTED);
            Order saved = orderRepository.save(order);
            orderEventPublisher.publishOrderCreated(saved);
            return saved;
        }

        ProductResponse product = productOpt.get();

        if (product.getQuantity() < order.getQuantity()) {
            order.setStatus(OrderStatus.REJECTED);
            Order saved = orderRepository.save(order);
            orderEventPublisher.publishOrderCreated(saved);
            return saved;
        }

        order.setStatus(OrderStatus.CONFIRMED);
        Order saved = orderRepository.save(order);
        orderEventPublisher.publishOrderCreated(saved);
        return saved;
    }
}