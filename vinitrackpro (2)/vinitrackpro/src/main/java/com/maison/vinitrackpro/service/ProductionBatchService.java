package com.maison.vinitrackpro.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.dto.CreateProductionBatchDTO;
import com.maison.vinitrackpro.dto.ProductionBatchDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.Product;
import com.maison.vinitrackpro.model.ProductionBatch;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.repository.ProductRepository;
import com.maison.vinitrackpro.repository.ProductionBatchRepository;
import com.maison.vinitrackpro.repository.UserRepository;

import jakarta.validation.ValidationException;

@Service
@Transactional
public class ProductionBatchService {

    @Autowired
    private ProductionBatchRepository productionBatchRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;

    public ProductionBatchDTO createBatch(CreateProductionBatchDTO createDTO, Long userId) {
        // Validate product exists
        Product product = productRepository.findById(createDTO.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + createDTO.getProductId()));
        
        // Validate user exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Validate batch number uniqueness
        if (productionBatchRepository.existsByBatchNumber(createDTO.getBatchNumber())) {
            throw new ValidationException("Batch number already exists: " + createDTO.getBatchNumber());
        }
        
        // Validate dates
        if (createDTO.getExpiryDate().isBefore(createDTO.getProductionDate())) {
            throw new ValidationException("Expiry date cannot be before production date");
        }

        ProductionBatch batch = ProductionBatch.builder()
            .batchNumber(createDTO.getBatchNumber())
            .product(product)
            .quantity(createDTO.getQuantity())
            .productionDate(createDTO.getProductionDate())
            .expiryDate(createDTO.getExpiryDate())
            .createdBy(user)
            .createdAt(LocalDateTime.now())
            .build();

        ProductionBatch savedBatch = productionBatchRepository.save(batch);
        return convertToDTO(savedBatch);
    }

    @Transactional(readOnly = true)
    public ProductionBatchDTO getBatchById(Long id) {
        ProductionBatch batch = productionBatchRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Production batch not found with id: " + id));
        return convertToDTO(batch);
    }

    @Transactional(readOnly = true)
    public List<ProductionBatchDTO> getAllBatches() {
        return productionBatchRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductionBatchDTO> getAllBatches(Pageable pageable) {
        Page<ProductionBatch> batchPage = productionBatchRepository.findAllByOrderByProductionDateDesc(pageable);
        List<ProductionBatchDTO> dtos = batchPage.getContent().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, batchPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ProductionBatchDTO> getBatchesByProduct(Long productId) {
        return productionBatchRepository.findByProductProductId(productId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public ProductionBatchDTO updateBatch(Long id, CreateProductionBatchDTO updateDTO) {
        ProductionBatch existingBatch = productionBatchRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Production batch not found with id: " + id));

        // Validate product exists
        Product product = productRepository.findById(updateDTO.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + updateDTO.getProductId()));

        // Validate batch number uniqueness (excluding current batch)
        if (!existingBatch.getBatchNumber().equals(updateDTO.getBatchNumber()) &&
            productionBatchRepository.existsByBatchNumber(updateDTO.getBatchNumber())) {
            throw new ValidationException("Batch number already exists: " + updateDTO.getBatchNumber());
        }

        existingBatch.setBatchNumber(updateDTO.getBatchNumber());
        existingBatch.setProduct(product);
        existingBatch.setQuantity(updateDTO.getQuantity());
        existingBatch.setProductionDate(updateDTO.getProductionDate());
        existingBatch.setExpiryDate(updateDTO.getExpiryDate());

        ProductionBatch updatedBatch = productionBatchRepository.save(existingBatch);
        return convertToDTO(updatedBatch);
    }

    public void deleteBatch(Long id) {
        if (!productionBatchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Production batch not found with id: " + id);
        }
        productionBatchRepository.deleteById(id);
    }

    private ProductionBatchDTO convertToDTO(ProductionBatch batch) {
        return ProductionBatchDTO.builder()
            .id(batch.getId())
            .batchNumber(batch.getBatchNumber())
            .productId(batch.getProduct().getProductId())
            .productName(batch.getProduct().getName())
            .productCode(batch.getProduct().getCode())
            .quantity(batch.getQuantity())
            .productionDate(batch.getProductionDate())
            .expiryDate(batch.getExpiryDate())
            .createdById(batch.getCreatedBy().getId())
            .createdByName(batch.getCreatedBy().getUsername())
            .createdAt(batch.getCreatedAt())
            .build();
    }
}
