package com.pq.grocery.generic;
import com.pq.grocery.enums.Status;
import lombok.Data;

@Data
public class GenericResponse {
    private Status status;
    private String message;
    private String statusCode;
}
