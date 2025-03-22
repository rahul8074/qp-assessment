package com.pq.grocery.entity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "grocery")
public class Grocery {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private String name;
   private double price;
   private int stock;
   @CreationTimestamp
   private LocalDateTime date;
}
