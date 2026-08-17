package com.stockora.inventory.repository;

import com.stockora.inventory.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class ProductRepositoryIT {

    @Container
    @ServiceConnection(name = "postgresql")
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private ProductRepository productRepository;

    @Test
    void savedProduct_canBeFoundBySku() {
        Product product = new Product("SKU-IT-001", "Integration Test Product", 7, 49.99);

        productRepository.save(product);

        Optional<Product> found = productRepository.findBySku("SKU-IT-001");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Integration Test Product");
        assertThat(found.get().getQuantity()).isEqualTo(7);
    }

    @Test
    void existsBySku_returnsTrueOnlyAfterSaving() {
        assertThat(productRepository.existsBySku("SKU-IT-002")).isFalse();

        productRepository.save(new Product("SKU-IT-002", "Another Product", 3, 15.50));

        assertThat(productRepository.existsBySku("SKU-IT-002")).isTrue();
    }
}