package com.stockora.inventory.repository;

import com.stockora.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    @Modifying
    @Query("UPDATE Product p SET p.quantity = p.quantity - :amount WHERE p.sku = :sku AND p.quantity >= :amount")
    int decrementStock(@Param("sku") String sku, @Param("amount") int amount);
}