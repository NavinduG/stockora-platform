package com.stockora.inventory.controller;

//import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import com.stockora.inventory.model.Product;
import com.stockora.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private JsonMapper objectMapper;

    @Test
    void getAllProducts_returnsListOfProducts() throws Exception {
        Product product = new Product("SKU-001", "Test Product", 5, 9.99);
        product.setId(1L);

        when(productRepository.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("SKU-001"))
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void getProductById_whenExists_returnsProduct() throws Exception {
        Product product = new Product("SKU-002", "Another Product", 3, 19.99);
        product.setId(2L);

        when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-002"));
    }

    @Test
    void getProductById_whenNotFound_returns404() throws Exception {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_whenSkuIsNew_returnsCreated() throws Exception {
        Product newProduct = new Product("SKU-003", "New Product", 10, 29.99);
        Product savedProduct = new Product("SKU-003", "New Product", 10, 29.99);
        savedProduct.setId(3L);

        when(productRepository.existsBySku("SKU-003")).thenReturn(false);
        when(productRepository.save(org.mockito.ArgumentMatchers.any(Product.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void createProduct_whenSkuAlreadyExists_returnsConflict() throws Exception {
        Product duplicate = new Product("SKU-001", "Duplicate", 1, 5.99);

        when(productRepository.existsBySku("SKU-001")).thenReturn(true);

        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isConflict());
    }
}