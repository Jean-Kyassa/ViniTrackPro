// package com.maison.vinitrackpro.controller;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.maison.vinitrackpro.dto.CreateProductionBatchDTO;
// import com.maison.vinitrackpro.dto.ProductionBatchDTO;
// import com.maison.vinitrackpro.dto.UserDTO;
// import com.maison.vinitrackpro.service.ProductionBatchService;
// import com.maison.vinitrackpro.service.UserService;

// import jakarta.validation.Valid;

// @RestController
// @RequestMapping("/api/production-batches")
// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
// public class ProductionBatchController {

//     @Autowired
//     private ProductionBatchService productionBatchService;
    
//     @Autowired
//     private UserService userService;

//     @PostMapping
//     public ResponseEntity<ProductionBatchDTO> createBatch(
//             @Valid @RequestBody CreateProductionBatchDTO createDTO,
//             Authentication authentication) {
//         Long userId = userService.getUserById(Long.parseLong(authentication.getName()))
//                                  .map(UserDTO::getId)
//                                  .orElseThrow(() -> new IllegalArgumentException("User not found"));
//         ProductionBatchDTO createdBatch = productionBatchService.createBatch(createDTO, userId);
//         return new ResponseEntity<>(createdBatch, HttpStatus.CREATED);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<ProductionBatchDTO> getBatchById(@PathVariable Long id) {
//         ProductionBatchDTO batch = productionBatchService.getBatchById(id);
//         return ResponseEntity.ok(batch);
//     }

//     @GetMapping
//     public ResponseEntity<List<ProductionBatchDTO>> getAllBatches(
//             @RequestParam(required = false) Long productId) {
//         List<ProductionBatchDTO> batches;
//         if (productId != null) {
//             batches = productionBatchService.getBatchesByProduct(productId);
//         } else {
//             batches = productionBatchService.getAllBatches();
//         }
//         return ResponseEntity.ok(batches);
//     }

//     @GetMapping("/paginated")
//     public ResponseEntity<Page<ProductionBatchDTO>> getAllBatchesPaginated(Pageable pageable) {
//         Page<ProductionBatchDTO> batches = productionBatchService.getAllBatches(pageable);
//         return ResponseEntity.ok(batches);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<ProductionBatchDTO> updateBatch(
//             @PathVariable Long id,
//             @Valid @RequestBody CreateProductionBatchDTO updateDTO) {
//         ProductionBatchDTO updatedBatch = productionBatchService.updateBatch(id, updateDTO);
//         return ResponseEntity.ok(updatedBatch);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
//         productionBatchService.deleteBatch(id);
//         return ResponseEntity.noContent().build();
//     }
// }




package com.maison.vinitrackpro.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.maison.vinitrackpro.dto.CreateProductionBatchDTO;
import com.maison.vinitrackpro.dto.ProductionBatchDTO;
import com.maison.vinitrackpro.service.ProductionBatchService;
import com.maison.vinitrackpro.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/production-batches")
// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@CrossOrigin(
    origins = {"http://localhost:5173", "http://localhost:3000"},
    allowCredentials = "true"
)
public class ProductionBatchController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductionBatchController.class);

    @Autowired
    private ProductionBatchService productionBatchService;
        
    @Autowired
    private UserService userService;
    
    @PostConstruct
    public void init() {
        logger.info("ProductionBatchController initialized - endpoints available at /api/production-batches");
    }

    @GetMapping
    public ResponseEntity<List<ProductionBatchDTO>> getAllBatches(
            @RequestParam(required = false) Long productId) {
        logger.info("GET /api/production-batches called with productId: {}", productId);
        List<ProductionBatchDTO> batches;
        if (productId != null) {
            batches = productionBatchService.getBatchesByProduct(productId);
        } else {
            batches = productionBatchService.getAllBatches();
        }
        return ResponseEntity.ok(batches);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductionBatchDTO>> getAllBatchesPaginated(Pageable pageable) {
        logger.info("GET /api/production-batches/paginated called");
        Page<ProductionBatchDTO> batches = productionBatchService.getAllBatches(pageable);
        return ResponseEntity.ok(batches);
    }

    // @PostMapping
    // public ResponseEntity<ProductionBatchDTO> createBatch(
    //         @Valid @RequestBody CreateProductionBatchDTO createDTO,
    //         Authentication authentication) {
    //     logger.info("POST /api/production-batches called");
    //     Long userId = userService.getUserById(Long.parseLong(authentication.getName()))
    //                              .map(UserDTO::getId)
    //                              .orElseThrow(() -> new IllegalArgumentException("User not found"));
    //     ProductionBatchDTO createdBatch = productionBatchService.createBatch(createDTO, userId);
    //     return new ResponseEntity<>(createdBatch, HttpStatus.CREATED);
    // }

    @PostMapping
public ResponseEntity<ProductionBatchDTO> createBatch(
        @Valid @RequestBody CreateProductionBatchDTO createDTO) {
    logger.info("POST /api/production-batches called");
    
    Long userId = createDTO.getUserId();
    ProductionBatchDTO createdBatch = productionBatchService.createBatch(createDTO, userId);
    return new ResponseEntity<>(createdBatch, HttpStatus.CREATED);
}


    @GetMapping("/{id}")
    public ResponseEntity<ProductionBatchDTO> getBatchById(@PathVariable Long id) {
        logger.info("GET /api/production-batches/{} called", id);
        ProductionBatchDTO batch = productionBatchService.getBatchById(id);
        return ResponseEntity.ok(batch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionBatchDTO> updateBatch(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductionBatchDTO updateDTO) {
        logger.info("PUT /api/production-batches/{} called", id);
        ProductionBatchDTO updatedBatch = productionBatchService.updateBatch(id, updateDTO);
        return ResponseEntity.ok(updatedBatch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        logger.info("DELETE /api/production-batches/{} called", id);
        productionBatchService.deleteBatch(id);
        return ResponseEntity.noContent().build();
    }
}
