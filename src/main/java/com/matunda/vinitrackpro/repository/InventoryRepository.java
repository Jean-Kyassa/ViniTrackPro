package com.matunda.vinitrackpro.repository;
import com.matunda.vinitrackpro.model.Inventory;
import com.matunda.vinitrackpro.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByProduct(Product product);
    List<Inventory> findByQuantityLessThanEqualAndStatus(Integer quantity, Inventory.InventoryStatus status);
    List<Inventory> findByExpiryDateBeforeAndStatus(LocalDate date, Inventory.InventoryStatus status);
    List<Inventory> findByStatus(Inventory.InventoryStatus status);
    
    @Query("SELECT i FROM Inventory i WHERE i.quantity <= i.minimumStockLevel AND i.status = 'AVAILABLE'")
    List<Inventory> findLowStockItems();
}
