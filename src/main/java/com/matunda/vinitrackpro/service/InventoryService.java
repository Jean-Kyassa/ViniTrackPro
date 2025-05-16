package com.matunda.vinitrackpro.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matunda.vinitrackpro.dto.InventoryDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Inventory;
import com.matunda.vinitrackpro.model.Product;
import com.matunda.vinitrackpro.model.User;
import com.matunda.vinitrackpro.repository.InventoryRepository;
import com.matunda.vinitrackpro.repository.ProductRepository;
import com.matunda.vinitrackpro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public List<InventoryDTO> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public InventoryDTO getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
        return convertToDTO(inventory);
    }

    public List<InventoryDTO> getInventoryByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return inventoryRepository.findByProduct(product).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<InventoryDTO> getLowStockItems() {
        return inventoryRepository.findLowStockItems().stream()
        .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<InventoryDTO> getExpiringItems(int daysThreshold) {
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        return inventoryRepository.findByExpiryDateBeforeAndStatus(thresholdDate, Inventory.InventoryStatus.AVAILABLE)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public InventoryDTO createInventory(InventoryDTO inventoryDTO)  {
        Product product = productRepository.findById(inventoryDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + inventoryDTO.getProductId()));
        
        User updatedBy = null;
        if (inventoryDTO.getUpdatedById() != null) {
            updatedBy = userRepository.findById(inventoryDTO.getUpdatedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + inventoryDTO.getUpdatedById()));
        }

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setMinimumStockLevel(inventoryDTO.getMinimumStockLevel());
        inventory.setLocation(inventoryDTO.getLocation());
        inventory.setBatchNumber(inventoryDTO.getBatchNumber());
        inventory.setExpiryDate(inventoryDTO.getExpiryDate().toLocalDate());
        inventory.setStatus(Inventory.InventoryStatus.valueOf(inventoryDTO.getStatus()));
        inventory.setUpdatedBy(updatedBy);

        Inventory savedInventory = inventoryRepository.save(inventory);
        
        // Check if this is a low stock situation and send notification
        if (savedInventory.getQuantity() <= savedInventory.getMinimumStockLevel()) {
            notificationService.createLowStockNotification(savedInventory);
        }
        
        return convertToDTO(savedInventory);
    }

    @Transactional
    public InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
        
        Product product = productRepository.findById(inventoryDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + inventoryDTO.getProductId()));
        
        User updatedBy = null;
        if (inventoryDTO.getUpdatedById() != null) {
            updatedBy = userRepository.findById(inventoryDTO.getUpdatedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + inventoryDTO.getUpdatedById()));
        }

        boolean wasLowStock = inventory.getQuantity() <= inventory.getMinimumStockLevel();
        
        inventory.setProduct(product);
        inventory.setQuantity(Long.valueOf(inventoryDTO.getQuantity().intValue()));
        inventory.setMinimumStockLevel(inventoryDTO.getMinimumStockLevel().intValue());
        inventory.setLocation(inventoryDTO.getLocation());
        inventory.setBatchNumber(inventoryDTO.getBatchNumber());
        inventory.setExpiryDate(inventoryDTO.getExpiryDate().toLocalDate());
        inventory.setStatus(Inventory.InventoryStatus.valueOf(inventoryDTO.getStatus()));
        inventory.setUpdatedBy(updatedBy);

        Inventory updatedInventory = inventoryRepository.save(inventory);
        
        // Check if this is now a low stock situation and send notification if it wasn't before
        boolean isNowLowStock = updatedInventory.getQuantity() <= updatedInventory.getMinimumStockLevel();
        if (!wasLowStock && isNowLowStock) {
            notificationService.createLowStockNotification(updatedInventory);
        }
        
        return convertToDTO(updatedInventory);
    }
    // New method for quantity adjustment:
    @Transactional
    public InventoryDTO adjustInventoryQuantity(Long id, Long adjustment, Long userId) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));

        User updatedBy = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        long newQuantity = inventory.getQuantity() + adjustment;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Resulting quantity cannot be negative");
        }

        inventory.setQuantity(newQuantity);
        inventory.setUpdatedBy(updatedBy);
        inventory.setLastStockUpdate(LocalDateTime.now());

        Inventory updatedInventory = inventoryRepository.save(inventory);

        // Check for low stock
        if (updatedInventory.getQuantity() <= updatedInventory.getMinimumStockLevel()) {
            notificationService.createLowStockNotification(updatedInventory);
        }

        return convertToDTO(updatedInventory);
    }
    @Transactional
    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventory item not found with id: " + id);
        }
        inventoryRepository.deleteById(id);
    }

    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setProductId(inventory.getProduct().getId());
        dto.setProductName(inventory.getProduct().getName());
        dto.setQuantity(Long.valueOf(inventory.getQuantity()));
        dto.setMinimumStockLevel(inventory.getMinimumStockLevel());
        dto.setLocation(inventory.getLocation());
        dto.setBatchNumber(inventory.getBatchNumber());
        dto.setExpiryDate(inventory.getExpiryDate().atStartOfDay());
        dto.setStatus(inventory.getStatus().name());
        
        if (inventory.getUpdatedBy() != null) {
            dto.setUpdatedAt(inventory.getUpdatedAt());
            //dto.setUpdatedAt(inventory.getUpdatedBy().getFullName());
        }
        
        dto.setCreatedAt(inventory.getCreatedAt());
        dto.setUpdatedAt(inventory.getUpdatedAt());
        return dto;
    }
}