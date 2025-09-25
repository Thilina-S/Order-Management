package com.example.order.repo;

import com.example.order.model.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodRepo extends JpaRepository<Good, Integer> {
    @Query(value = "SELECT * FROM good WHERE item_id = ?1", nativeQuery = true)
    Good getGoodByItemId(Integer itemId);
}
