package com.stockora.order.service;

import com.stockora.order.client.InventoryClient;
import com.stockora.order.client.ProductResponse;
import com.stockora.order.model.Order;
import com.stockora.order.model.OrderStatus;
import com.stockora.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    public Order createOrder(Order order) {
        Optional<ProductResponse> productOpt = inventoryClient.getProductBySku(order.getProductSku());

        if (productOpt.isEmpty()) {
            order.setStatus(OrderStatus.REJECTED);
            return orderRepository.save(order);
        }

        ProductResponse product = productOpt.get();

        if (product.getQuantity() < order.getQuantity()) {
            order.setStatus(OrderStatus.REJECTED);
            return orderRepository.save(order);
        }

        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }
}