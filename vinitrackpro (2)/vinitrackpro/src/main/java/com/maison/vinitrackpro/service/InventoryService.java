package com.maison.vinitrackpro.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.maison.vinitrackpro.dto.InventoryItemDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.InventoryItem;
import com.maison.vinitrackpro.model.ItemCategory;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.repository.InventoryRepository;
import com.maison.vinitrackpro.repository.UserRepository;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public Page<InventoryItemDTO> getAllItems(Pageable pageable, String search, ItemCategory category) {
        Page<InventoryItem> items;
        if (search != null && !search.isEmpty()) {
            items = inventoryRepository.findByItemNameContaining(search, pageable);
        } else if (category != null) {
            items = inventoryRepository.findByCategory(category, pageable);
        } else {
            items = inventoryRepository.findAll(pageable);
        }
        return items.map(this::convertToDto);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public InventoryItemDTO getItemById(Long id) {
        InventoryItem item = inventoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
        return convertToDto(item);
    }

    // Add these methods to your existing InventoryService class
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public Map<String, Object> getInventoryStats() {
    List<InventoryItem> allItems = inventoryRepository.findAll();
    List<InventoryItem> lowStockItems = inventoryRepository.findByCurrentQuantityLessThanEqual(10);
    
    double totalValue = allItems.stream()
        .mapToDouble(item -> item.getCurrentQuantity() * item.getCostPrice())
        .sum();
    
    Map<String, Object> stats = new HashMap<>();
    stats.put("totalItems", allItems.size());
    stats.put("lowStockItems", lowStockItems.size());
    stats.put("totalInventoryValue", totalValue);
    
    return stats;
}

    
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public InventoryItemDTO createItem(InventoryItemDTO itemDTO, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        InventoryItem item = new InventoryItem();
        // Set fields from DTO
        item.setItemName(itemDTO.getItemName());
        item.setSku(itemDTO.getSku());
        item.setCategory(itemDTO.getCategory());
        item.setUnitType(itemDTO.getUnitType());
        item.setInitialQuantity(itemDTO.getInitialQuantity());
        item.setCurrentQuantity(itemDTO.getInitialQuantity()); // Set current to initial
        item.setMinimumStockLevel(itemDTO.getMinimumStockLevel());
        item.setManufacturingDate(itemDTO.getManufacturingDate());
        item.setExpiryDate(itemDTO.getExpiryDate());
        item.setCostPrice(itemDTO.getCostPrice());
        item.setSellingPrice(itemDTO.getSellingPrice());
        item.setDescription(itemDTO.getDescription());
        item.setStorageLocation(itemDTO.getStorageLocation());
        
        item.setCreatedBy(user);
        item.setCreatedAt(LocalDateTime.now());
        
        InventoryItem savedItem = inventoryRepository.save(item);
        return convertToDto(savedItem);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public InventoryItemDTO updateItem(Long id, InventoryItemDTO itemDTO) {
        InventoryItem item = inventoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
        
        // Update fields from DTO
        item.setItemName(itemDTO.getItemName());
        item.setSku(itemDTO.getSku());
        item.setCategory(itemDTO.getCategory());
        item.setUnitType(itemDTO.getUnitType());
        item.setInitialQuantity(itemDTO.getInitialQuantity());
        item.setCurrentQuantity(itemDTO.getCurrentQuantity());
        item.setMinimumStockLevel(itemDTO.getMinimumStockLevel());
        item.setManufacturingDate(itemDTO.getManufacturingDate());
        item.setExpiryDate(itemDTO.getExpiryDate());
        item.setCostPrice(itemDTO.getCostPrice());
        item.setSellingPrice(itemDTO.getSellingPrice());
        item.setDescription(itemDTO.getDescription());
        item.setStorageLocation(itemDTO.getStorageLocation());
        
        InventoryItem updatedItem = inventoryRepository.save(item);
        return convertToDto(updatedItem);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public void deleteItem(Long id) {
        InventoryItem item = inventoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
        inventoryRepository.delete(item);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public List<InventoryItemDTO> getLowStockItems() {
        List<InventoryItem> items = inventoryRepository.findByCurrentQuantityLessThanEqual(10); // Threshold for low stock
        return items.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private InventoryItemDTO convertToDto(InventoryItem item) {
        InventoryItemDTO dto = new InventoryItemDTO();
        // Set fields from entity
        dto.setId(item.getId());
        dto.setItemName(item.getItemName());
        dto.setSku(item.getSku());
        dto.setCategory(item.getCategory());
        dto.setUnitType(item.getUnitType());
        dto.setInitialQuantity(item.getInitialQuantity());
        dto.setCurrentQuantity(item.getCurrentQuantity());
        dto.setMinimumStockLevel(item.getMinimumStockLevel());
        dto.setManufacturingDate(item.getManufacturingDate());
        dto.setExpiryDate(item.getExpiryDate());
        dto.setCostPrice(item.getCostPrice());
        dto.setSellingPrice(item.getSellingPrice());
        dto.setDescription(item.getDescription());
        dto.setStorageLocation(item.getStorageLocation());
        return dto;
    }
}
