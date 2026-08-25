package com.stockora.order.event;

import java.io.Serializable;

public class OrderCreatedEvent implements Serializable {

    private Long orderId;
    private String productSku;
    private Integer quantity;
    private String customerName;
    private String status;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId, String productSku, Integer quantity, String customerName, String status) {
        this.orderId = orderId;
        this.productSku = productSku;
        this.quantity = quantity;
        this.customerName = customerName;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}