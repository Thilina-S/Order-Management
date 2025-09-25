package com.example.inventory.repo;

import com.example.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Integer> {

    // Find items by availability status
    List<Inventory> findByAvailability(Integer availability);

    // Find items by product ID
    List<Inventory> findByProductId(Integer productId);

    // Custom query to check if product exists
    @Query("SELECT COUNT(i) > 0 FROM Inventory i WHERE i.productId = :productId")
    boolean existsByProductId(@Param("productId") Integer productId);
}