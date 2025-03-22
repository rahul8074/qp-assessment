package com.pq.grocery.service;

import com.pq.grocery.entity.Grocery;
import com.pq.grocery.entity.Orders;
import com.pq.grocery.entity.OrderItem;
import com.pq.grocery.repository.GroceryRepository;
import com.pq.grocery.repository.OrderItemRepository;
import com.pq.grocery.repository.OrderRepository;
import com.pq.grocery.request.OrderItemRequest;
import com.pq.grocery.request.OrderRequest;
import com.pq.grocery.response.OrderDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GroceryRepository groceryRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public OrderDetailsResponse placeOrder(OrderRequest request) {
        // Fetch groceries and check stock availability
        Map<Long, Grocery> groceryMap = fetchGroceries(request);

        // Check if all requested items have sufficient stock
        List<OrderDetailsResponse.OrderItemResponse> insufficientStockItems = checkStockAvailability(request, groceryMap);

        // If stock is insufficient for any items, return the response with the unavailable items
        if (!insufficientStockItems.isEmpty()) {
            return prepareInsufficientStockResponse(insufficientStockItems);
        }

        // Create a new order and save it
        Orders orders = createOrder(request);

        // Create and save order items, reducing stock
        List<OrderItem> orderItems = createOrderItems(request, orders, groceryMap);

        // Save all order items
        orderItemRepository.saveAll(orderItems);

        return prepareOrderResponse(orders, orderItems);
    }

    private Map<Long, Grocery> fetchGroceries(OrderRequest request) {
        List<Long> groceryIds = request.getItems().stream()
                .map(OrderItemRequest::getGroceryId)
                .collect(Collectors.toList());

        List<Grocery> groceries = groceryRepository.findAllById(groceryIds);

        // Creating a map for quick lookup of groceries by their ID
        Map<Long, Grocery> groceryMap = new HashMap<>();
        for (Grocery grocery : groceries) {
            groceryMap.put(grocery.getId(), grocery);
        }

        return groceryMap;
    }

    private List<OrderDetailsResponse.OrderItemResponse> checkStockAvailability(OrderRequest request, Map<Long, Grocery> groceryMap) {
        List<OrderDetailsResponse.OrderItemResponse> insufficientStockItems = new ArrayList<>();

        for (OrderItemRequest orderItem : request.getItems()) {
            Long groceryId = orderItem.getGroceryId();
            int requestedQuantity = orderItem.getQuantity();
            Grocery grocery = groceryMap.get(groceryId);

            if (grocery == null) {
                throw new RuntimeException("Grocery item not found with id: " + groceryId);
            }

            int availableQuantity = grocery.getStock();

            if (requestedQuantity > availableQuantity) {
                insufficientStockItems.add(new OrderDetailsResponse.OrderItemResponse(
                        groceryId, grocery.getName(), requestedQuantity, availableQuantity));
            }
        }

        return insufficientStockItems;
    }

    private OrderDetailsResponse prepareInsufficientStockResponse(List<OrderDetailsResponse.OrderItemResponse> insufficientStockItems) {
        OrderDetailsResponse response = new OrderDetailsResponse();
        response.setAvailableQuantities(insufficientStockItems);
        response.setMessage("Some items are not available in sufficient quantity");
        return response;
    }

    private Orders createOrder(OrderRequest request) {
        Orders orders = new Orders();
        StringBuilder groceryIds = new StringBuilder();
        request.getItems().forEach(item -> {
            groceryIds.append(item.getGroceryId()).append(",");
        });
        orders.setGroceryIds(groceryIds.toString());
        orders.setUserId(request.getUserId());
        return orderRepository.save(orders); // Save the order first to generate ID
    }

    private List<OrderItem> createOrderItems(OrderRequest request, Orders orders, Map<Long, Grocery> groceryMap) {
        return request.getItems().stream().map(itemRequest -> {
            Grocery grocery = groceryMap.get(itemRequest.getGroceryId());
            if (grocery == null) {
                throw new RuntimeException("Grocery item not found with id: " + itemRequest.getGroceryId());
            }

            // Reduce the stock
            grocery.setStock(grocery.getStock() - itemRequest.getQuantity());
            groceryRepository.save(grocery);  // Save the updated grocery stock

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrders(orders);
            orderItem.setGrocery(grocery);
            orderItem.setQuantity(itemRequest.getQuantity());

            return orderItem;
        }).collect(Collectors.toList());
    }

    private OrderDetailsResponse prepareOrderResponse(Orders orders, List<OrderItem> orderItems) {
        OrderDetailsResponse response = new OrderDetailsResponse();
        response.setOrderId(orders.getId());
        response.setUserId(orders.getUserId());
        response.setItems(orderItems.stream()
                .map(item -> new OrderDetailsResponse.OrderItemResponse(
                        item.getGrocery().getId(), item.getGrocery().getName(), item.getQuantity()))
                .collect(Collectors.toList()));
        return response;
    }
}