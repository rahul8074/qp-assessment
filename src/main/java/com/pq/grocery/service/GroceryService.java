package com.pq.grocery.service;

import com.pq.grocery.entity.Grocery;
import com.pq.grocery.enums.Status;
import com.pq.grocery.repository.GroceryRepository;
import com.pq.grocery.request.AddGroceryRequest;
import com.pq.grocery.response.AddGroceryResponse;
import com.pq.grocery.response.GroceryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroceryService {
    @Autowired
    private GroceryRepository groceryRepository;

    @Transactional
    public AddGroceryResponse addGrocery(AddGroceryRequest request) {
        AddGroceryResponse response = new AddGroceryResponse();
        validateRequest(request);

        Grocery grocery = new Grocery();
        grocery.setName(request.getName());
        grocery.setPrice(request.getPrice());
        grocery.setStock(request.getStock());

        Grocery existingGrocery = groceryRepository.findByName(request.getName());

        // update the stock and price
        if (existingGrocery != null) {
            existingGrocery.setPrice(request.getPrice());
            existingGrocery.setStock(request.getStock() + existingGrocery.getStock());
            response.setMessage(request.getName() + " price and stock updated in db");
            response.setGrocery(existingGrocery);
            groceryRepository.save(existingGrocery);
            response.setStatus(Status.SUCCESS);
            return response;
        }

        response.setGrocery(grocery);
        response.setMessage("Grocery saved in db");
        response.setStatus(Status.SUCCESS);
        groceryRepository.save(grocery);
        return response;
    }

    public List<GroceryResponse> getAllGroceries () {
      List<Grocery> groceries =  groceryRepository.findAll();
      List<GroceryResponse> allGroceryList = new ArrayList<>();
      groceries.forEach((g) -> {
          GroceryResponse grocery = new GroceryResponse();
          grocery.setId(g.getId());
          grocery.setStock(g.getStock());
          grocery.setPrice(g.getPrice());
          grocery.setName(g.getName());
          allGroceryList.add(grocery);
      });
      return allGroceryList;
    }

    public List<GroceryResponse> availableGroceries () {
        List<Grocery> groceries =  groceryRepository.findAvailableStock();
        List<GroceryResponse> allGroceryList = new ArrayList<>();
        groceries.forEach((g) -> {
            GroceryResponse grocery = new GroceryResponse();
            grocery.setId(g.getId());
            grocery.setStock(g.getStock());
            grocery.setPrice(g.getPrice());
            grocery.setName(g.getName());
            allGroceryList.add(grocery);
        });
        return allGroceryList;
    }
    public String deleteGrocery(Long id) {
        boolean isDeleted = false;
        if (groceryRepository.existsById(id)) {
            groceryRepository.deleteById(id);
            isDeleted = true;
        }
        if (isDeleted) {
            return "Grocery item with ID " + id + " deleted successfully.";
        } else {
            return "Grocery item with ID " + id + " not found.";
        }
    }
    public String updateGrocery(Long id, Grocery request) {
        Optional<Grocery> optionalGrocery = groceryRepository.findById(id);
        boolean isUpdated = false;
        if (optionalGrocery.isPresent()) {
            Grocery existingGrocery = optionalGrocery.get();
            existingGrocery.setName(request.getName());
            existingGrocery.setPrice(request.getPrice());
            existingGrocery.setStock(request.getStock());
            groceryRepository.save(existingGrocery);
            isUpdated = true;
        }
        if (isUpdated) {
            return "Grocery item updated successfully.";
        } else {
            return "Grocery item not found.";
        }
    }
    private void validateRequest(AddGroceryRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty or null");
        }
        if (request.getStock() <= 0) {
            throw new IllegalArgumentException("Stock must be at least 1");
        }
        if (request.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be at least 0");
        }
    }
    public String updateStock(Long id, int stockChange) {
        Optional<Grocery> optionalGrocery = groceryRepository.findById(id);
        boolean isUpdated = false;
        if (optionalGrocery.isPresent()) {
            Grocery grocery = optionalGrocery.get();
            grocery.setStock(grocery.getStock() + stockChange); // Increment or decrement stock
            groceryRepository.save(grocery);
            isUpdated =  true;
        }
        if (isUpdated) {
            return "Stock updated successfully.";
        } else {
            return "Grocery item not found.";
        }
    }
}
