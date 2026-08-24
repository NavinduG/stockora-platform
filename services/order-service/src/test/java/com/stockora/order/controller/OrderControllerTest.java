package com.stockora.order.controller;

import com.stockora.order.model.Order;
import com.stockora.order.model.OrderStatus;
import com.stockora.order.repository.OrderRepository;
import com.stockora.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private OrderRepository orderRepository;

    @Autowired
    private JsonMapper objectMapper;

    @Test
    void getAllOrders_returnsListOfOrders() throws Exception {
        Order order = new Order("SKU-001", 2, "John Doe");
        order.setId(1L);
        order.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findAll()).thenReturn(List.of(order));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productSku").value("SKU-001"))
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
    }

    @Test
    void getOrderById_whenExists_returnsOrder() throws Exception {
        Order order = new Order("SKU-002", 1, "Jane Smith");
        order.setId(2L);
        order.setStatus(OrderStatus.REJECTED);

        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    void getOrderById_whenNotFound_returns404() throws Exception {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOrder_delegatesToOrderServiceAndReturnsCreated() throws Exception {
        Order requestOrder = new Order("SKU-003", 3, "Alice");
        Order savedOrder = new Order("SKU-003", 3, "Alice");
        savedOrder.setId(3L);
        savedOrder.setStatus(OrderStatus.CONFIRMED);

        when(orderService.createOrder(any(Order.class))).thenReturn(savedOrder);

        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }
}