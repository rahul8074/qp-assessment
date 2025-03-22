package com.pq.grocery.request;
import lombok.Data;
@Data
public class AddGroceryRequest {
    private String name;
    private int stock;
    private double price;
}
