package com.stockora.inventory.listener;

import com.stockora.inventory.config.RabbitMQConfig;
import com.stockora.inventory.event.OrderCreatedEvent;
import com.stockora.inventory.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderCreatedListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final ProductRepository productRepository;

    public OrderCreatedListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.INVENTORY_STOCK_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        if (!"CONFIRMED".equals(event.getStatus())) {
            logger.info("Ignoring order #{} with status {} - no stock change needed",
                    event.getOrderId(), event.getStatus());
            return;
        }

        int updated = productRepository.decrementStock(event.getProductSku(), event.getQuantity());

        if (updated == 1) {
            logger.info("✅ Stock decremented for order #{}: {} x {}",
                    event.getOrderId(), event.getQuantity(), event.getProductSku());
        } else {
            logger.warn("⚠️ Could not decrement stock for order #{}: {} x {} - insufficient stock or product not found",
                    event.getOrderId(), event.getQuantity(), event.getProductSku());
        }
    }
}