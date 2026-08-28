package com.stockora.notification.listener;

import com.stockora.notification.config.RabbitMQConfig;
import com.stockora.notification.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        if ("CONFIRMED".equals(event.getStatus())) {
            logger.info("📦 Notification: Order #{} CONFIRMED for {} — {} x {} — sending confirmation email (simulated)",
                    event.getOrderId(), event.getCustomerName(), event.getQuantity(), event.getProductSku());
        } else {
            logger.info("⚠️ Notification: Order #{} {} for {} — {} x {} — sending rejection notice (simulated)",
                    event.getOrderId(), event.getStatus(), event.getCustomerName(), event.getQuantity(), event.getProductSku());
        }
    }
}