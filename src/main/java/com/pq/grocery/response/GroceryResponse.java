package com.pq.grocery.response;

import lombok.Data;

@Data
public class GroceryResponse {
    private Long id;
    private String name;
    private double price;
    private int stock;
}
