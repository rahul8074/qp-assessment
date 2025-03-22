package com.pq.grocery.response;

import com.pq.grocery.entity.Grocery;
import com.pq.grocery.generic.GenericResponse;
import lombok.Data;

import java.util.List;

@Data
public class AddGroceryResponse extends GenericResponse {
    private Grocery grocery;
}
