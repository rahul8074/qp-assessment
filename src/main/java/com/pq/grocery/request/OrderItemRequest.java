package com.pq.grocery.request;


import lombok.Data;

@Data
public class OrderItemRequest {
    private Long groceryId;
    private int quantity;

    public OrderItemRequest(Long groceryId,int quantity) {
        this.groceryId = groceryId;
        this.quantity = quantity;
    }
}
