package com.pq.grocery.response;


import lombok.Data;

import java.util.List;

@Data
public class OrderDetailsResponse {
    private Long orderId;
    private Long userId;
    private List<OrderItemResponse> items;
    private List<OrderItemResponse> availableQuantities;
    private String message;
    @Data
    public static class OrderItemResponse {
        private Long groceryId;
        private String groceryName;
        private int quantity;
        private Integer availableQuantity;
        public OrderItemResponse(Long groceryId, String groceryName, int quantity) {
            this.groceryId = groceryId;
            this.groceryName = groceryName;
            this.quantity = quantity;
        }
        public OrderItemResponse(Long groceryId, String groceryName, int quantity, Integer availableQuantity) {
            this.groceryId = groceryId;
            this.groceryName = groceryName;
            this.quantity = quantity;
            this.availableQuantity = availableQuantity;
        }

    }
}
