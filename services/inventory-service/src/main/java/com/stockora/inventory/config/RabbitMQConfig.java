package com.stockora.inventory.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String INVENTORY_STOCK_QUEUE = "inventory.stock.queue";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue inventoryStockQueue() {
        return QueueBuilder.durable(INVENTORY_STOCK_QUEUE)
                .withArgument("x-dead-letter-exchange", "inventory.dlx")
                .withArgument("x-dead-letter-routing-key", "inventory.stock.dlq")
                .build();
    }

    @Bean
    public Binding inventoryStockBinding(Queue inventoryStockQueue, TopicExchange orderExchange) {
        return BindingBuilder
                .bind(inventoryStockQueue)
                .to(orderExchange)
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, JacksonJsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        return factory;
    }

    @Bean
    public TopicExchange inventoryDeadLetterExchange() {
        return new TopicExchange("inventory.dlx");
    }

    @Bean
    public Queue inventoryStockDeadLetterQueue() {
        return QueueBuilder.durable("inventory.stock.dlq").build();
    }

    @Bean
    public Binding inventoryDlqBinding(Queue inventoryStockDeadLetterQueue, TopicExchange inventoryDeadLetterExchange) {
        return BindingBuilder.bind(inventoryStockDeadLetterQueue).to(inventoryDeadLetterExchange).with("inventory.stock.dlq");
    }
}