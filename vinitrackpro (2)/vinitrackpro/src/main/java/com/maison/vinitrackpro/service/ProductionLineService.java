package com.maison.vinitrackpro.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.dto.CreateProductionLineDTO;
import com.maison.vinitrackpro.dto.ProductionLineDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.LineStatus;
import com.maison.vinitrackpro.model.LineType;
import com.maison.vinitrackpro.model.ProductionLine;
import com.maison.vinitrackpro.repository.ProductionLineRepository;

import jakarta.validation.ValidationException;

@Service
@Transactional
public class ProductionLineService {

    @Autowired
    private ProductionLineRepository productionLineRepository;

    public ProductionLineDTO createProductionLine(CreateProductionLineDTO createDTO) {
        // Validate name uniqueness
        if (productionLineRepository.existsByName(createDTO.getName())) {
            throw new ValidationException("Production line name already exists: " + createDTO.getName());
        }

        ProductionLine productionLine = ProductionLine.builder()
            .name(createDTO.getName())
            .type(createDTO.getType())
            .efficiency(createDTO.getEfficiency())
            .lastMaintenance(createDTO.getLastMaintenance())
            .status(createDTO.getStatus())
            .build();

        ProductionLine savedLine = productionLineRepository.save(productionLine);
        return convertToDTO(savedLine);
    }

    @Transactional(readOnly = true)
    public ProductionLineDTO getProductionLineById(Long id) {
        ProductionLine productionLine = productionLineRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Production line not found with id: " + id));
        return convertToDTO(productionLine);
    }

    @Transactional(readOnly = true)
    public List<ProductionLineDTO> getAllProductionLines() {
        return productionLineRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductionLineDTO> getProductionLinesByType(LineType type) {
        return productionLineRepository.findByType(type).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductionLineDTO> getProductionLinesByStatus(LineStatus status) {
        return productionLineRepository.findByStatus(status).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductionLineDTO> getLinesNeedingMaintenance(double efficiencyThreshold) {
        return productionLineRepository.findLinesNeedingMaintenance(efficiencyThreshold).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public ProductionLineDTO updateProductionLine(Long id, CreateProductionLineDTO updateDTO) {
        ProductionLine existingLine = productionLineRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Production line not found with id: " + id));

        // Validate name uniqueness (excluding current line)
        if (!existingLine.getName().equals(updateDTO.getName()) &&
            productionLineRepository.existsByName(updateDTO.getName())) {
            throw new ValidationException("Production line name already exists: " + updateDTO.getName());
        }

        existingLine.setName(updateDTO.getName());
        existingLine.setType(updateDTO.getType());
        existingLine.setEfficiency(updateDTO.getEfficiency());
        existingLine.setLastMaintenance(updateDTO.getLastMaintenance());
        existingLine.setStatus(updateDTO.getStatus());

        ProductionLine updatedLine = productionLineRepository.save(existingLine);
        return convertToDTO(updatedLine);
    }

    public void deleteProductionLine(Long id) {
        if (!productionLineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Production line not found with id: " + id);
        }
        productionLineRepository.deleteById(id);
    }

    private ProductionLineDTO convertToDTO(ProductionLine productionLine) {
        return ProductionLineDTO.builder()
            .id(productionLine.getId())
            .name(productionLine.getName())
            .type(productionLine.getType())
            .efficiency(productionLine.getEfficiency())
            .lastMaintenance(productionLine.getLastMaintenance())
            .status(productionLine.getStatus())
            .build();
    }
}
