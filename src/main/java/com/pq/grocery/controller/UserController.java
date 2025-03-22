package com.pq.grocery.controller;

import com.pq.grocery.request.OrderRequest;
import com.pq.grocery.response.GroceryResponse;
import com.pq.grocery.response.OrderDetailsResponse;
import com.pq.grocery.service.GroceryService;
import com.pq.grocery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private GroceryService groceryService;

    @Autowired
    private UserService userService;

    @GetMapping("/available-groceries")
    public List<GroceryResponse> getAvailableGrocery() {
        return groceryService.availableGroceries();
    }
    @PostMapping("/place-order")
    public OrderDetailsResponse placeOrder(@RequestBody OrderRequest request) {
        return userService.placeOrder(request);
    }
}
