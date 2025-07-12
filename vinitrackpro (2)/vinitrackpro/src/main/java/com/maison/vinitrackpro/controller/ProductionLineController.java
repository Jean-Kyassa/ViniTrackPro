package com.maison.vinitrackpro.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maison.vinitrackpro.dto.CreateProductionLineDTO;
import com.maison.vinitrackpro.dto.ProductionLineDTO;
import com.maison.vinitrackpro.model.LineStatus;
import com.maison.vinitrackpro.model.LineType;
import com.maison.vinitrackpro.service.ProductionLineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/production-lines")
// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@CrossOrigin(
    origins = {"http://localhost:5173", "http://localhost:3000"},
    allowCredentials = "true"
)
public class ProductionLineController {

    @Autowired
    private ProductionLineService productionLineService;

    @PostMapping
    public ResponseEntity<ProductionLineDTO> createProductionLine(@Valid @RequestBody CreateProductionLineDTO createDTO) {
        ProductionLineDTO createdLine = productionLineService.createProductionLine(createDTO);
        return new ResponseEntity<>(createdLine, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionLineDTO> getProductionLineById(@PathVariable Long id) {
        ProductionLineDTO productionLine = productionLineService.getProductionLineById(id);
        return ResponseEntity.ok(productionLine);
    }

    @GetMapping
    public ResponseEntity<List<ProductionLineDTO>> getAllProductionLines(
            @RequestParam(required = false) LineType type,
            @RequestParam(required = false) LineStatus status,
            @RequestParam(required = false) Double efficiencyThreshold) {
        List<ProductionLineDTO> lines;
        
        if (efficiencyThreshold != null) {
            lines = productionLineService.getLinesNeedingMaintenance(efficiencyThreshold);
        } else if (type != null) {
            lines = productionLineService.getProductionLinesByType(type);
        } else if (status != null) {
            lines = productionLineService.getProductionLinesByStatus(status);
        } else {
            lines = productionLineService.getAllProductionLines();
        }
        
        return ResponseEntity.ok(lines);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionLineDTO> updateProductionLine(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductionLineDTO updateDTO) {
        ProductionLineDTO updatedLine = productionLineService.updateProductionLine(id, updateDTO);
        return ResponseEntity.ok(updatedLine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductionLine(@PathVariable Long id) {
        productionLineService.deleteProductionLine(id);
        return ResponseEntity.noContent().build();
    }
}
