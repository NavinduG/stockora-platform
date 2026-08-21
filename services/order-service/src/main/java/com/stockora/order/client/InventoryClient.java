package com.stockora.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(@Value("${inventory.service.url}") String inventoryServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .build();
    }

    public Optional<ProductResponse> getProductBySku(String sku) {
        try {
            ProductResponse[] products = restClient.get()
                    .uri("/products")
                    .retrieve()
                    .body(ProductResponse[].class);

            if (products == null) {
                return Optional.empty();
            }

            for (ProductResponse product : products) {
                if (product.getSku().equals(sku)) {
                    return Optional.of(product);
                }
            }
            return Optional.empty();

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}