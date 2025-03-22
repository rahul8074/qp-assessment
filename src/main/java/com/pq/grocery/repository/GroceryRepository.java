package com.pq.grocery.repository;

import com.pq.grocery.entity.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroceryRepository extends JpaRepository<Grocery,Long> {
    Grocery findByName(String name);
    @Query(nativeQuery = true,value = "SELECT * FROM grocery where stock > 0")
    List<Grocery> findAvailableStock();
}
