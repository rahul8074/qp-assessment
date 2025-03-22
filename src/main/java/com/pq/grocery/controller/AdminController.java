package com.pq.grocery.controller;

import com.pq.grocery.entity.Grocery;
import com.pq.grocery.enums.Status;
import com.pq.grocery.request.AddGroceryRequest;
import com.pq.grocery.response.AddGroceryResponse;
import com.pq.grocery.response.GroceryResponse;
import com.pq.grocery.service.GroceryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GroceryService groceryService;

    @GetMapping("/health")
    public String healthCheck() {
        return "Grocery Booking system is up.";
    }
    @PostMapping("/add-grocery")
    public AddGroceryResponse addGrocery( @RequestBody AddGroceryRequest request) {
        return groceryService.addGrocery(request);
    }

    @GetMapping("/groceries")
    public List<GroceryResponse> getAllGroceries() {
        return groceryService.getAllGroceries();
    }

    @DeleteMapping("/grocery/{id}")
    public String deleteGrocery(@PathVariable Long id) {
        return groceryService.deleteGrocery(id);
    }
    @PutMapping("/update-grocery/{id}")
    public String updateGrocery(@PathVariable Long id, @RequestBody Grocery request) {
        return groceryService.updateGrocery(id, request);

    }
    @PatchMapping("/update-stock/{id}")
    public String updateStock(@PathVariable Long id, @RequestParam int stockChange) {
        return groceryService.updateStock(id, stockChange);

    }
}
