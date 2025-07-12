package com.maison.vinitrackpro.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.InventoryItem;
import com.maison.vinitrackpro.model.ItemCategory;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    Page<InventoryItem> findByItemNameContaining(String itemName, Pageable pageable);
    Page<InventoryItem> findByCategory(ItemCategory category, Pageable pageable);
    List<InventoryItem> findByCurrentQuantityLessThanEqual(int quantity);
}