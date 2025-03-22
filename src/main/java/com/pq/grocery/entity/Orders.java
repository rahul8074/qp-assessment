package com.pq.grocery.entity;
import lombok.Data;
import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String groceryIds;

    public void setGroceryIdsList(List<Integer> groceryIdsList) {
        this.groceryIds = groceryIdsList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    // ✅ Convert String back to List<Integer> when retrieving
    public List<Integer> getGroceryIdsList() {
        return Arrays.stream(this.groceryIds.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
