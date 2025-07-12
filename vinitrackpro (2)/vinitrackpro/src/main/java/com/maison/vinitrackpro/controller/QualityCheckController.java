package com.maison.vinitrackpro.controller;

// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.format.annotation.DateTimeFormat;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.validation.FieldError;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PatchMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.maison.vinitrackpro.dto.QualityCheckDTO;
// import com.maison.vinitrackpro.dto.QualityCheckDTO.CreateQualityCheckDTO;
// import com.maison.vinitrackpro.model.QualityCheckType;
// import com.maison.vinitrackpro.model.QualityStatus;
// import com.maison.vinitrackpro.service.QualityCheckService;

// import jakarta.persistence.EntityNotFoundException;
// import jakarta.validation.Valid;

// @RestController
// @RequestMapping("/api/quality-checks")
// // @CrossOrigin(origins = "*")
// public class QualityCheckController {

//      private final QualityCheckService qualityCheckService;
    
//     public QualityCheckController(QualityCheckService qualityCheckService) {
//         this.qualityCheckService = qualityCheckService;
//     }
    
//     @GetMapping
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<List<QualityCheckDTO>> getAllQualityChecks() {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.getAllQualityChecks();
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/{id}")
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<QualityCheckDTO> getQualityCheckById(@PathVariable Long id) {
//         Optional<QualityCheckDTO> qualityCheck = qualityCheckService.getQualityCheckById(id);
//         return qualityCheck.map(ResponseEntity::ok)
//                           .orElse(ResponseEntity.notFound().build());
//     }
    
//     @GetMapping("/batch/{batchId}")
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByBatch(@PathVariable Long batchId) {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByBatchId(batchId);
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/status/{status}")
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByStatus(@PathVariable QualityStatus status) {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByStatus(status);
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/status/{status}/paginated")
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<Page<QualityCheckDTO>> getQualityChecksByStatusPaginated(
//             @PathVariable QualityStatus status,
//             @RequestParam(defaultValue = "0") int page,
//             @RequestParam(defaultValue = "10") int size,
//             @RequestParam(defaultValue = "checkDate") String sortBy,
//             @RequestParam(defaultValue = "desc") String sortDir) {
        
//         Sort sort = sortDir.equalsIgnoreCase("desc") ? 
//                    Sort.by(sortBy).descending() : 
//                    Sort.by(sortBy).ascending();
        
//         Pageable pageable = PageRequest.of(page, size, sort);
//         Page<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByStatus(status, pageable);
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/type/{type}")
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByType(@PathVariable QualityCheckType type) {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByType(type);
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/user/{userId}")
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByUser(@PathVariable Long userId) {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByUser(userId);
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/batch-number/{batchNumber}")
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByBatchNumber(@PathVariable String batchNumber) {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByBatchNumber(batchNumber);
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/search")
// @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<List<QualityCheckDTO>> searchQualityChecksByName(@RequestParam String name) {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.searchQualityChecksByName(name);
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/date-range")
// @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByDateRange(
//             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByDateRange(startDate, endDate);
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/batch/{batchId}/status/{status}")
// @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByBatchAndStatus(
//             @PathVariable Long batchId, 
//             @PathVariable QualityStatus status) {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByBatchIdAndStatus(batchId, status);
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/statistics")
// @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<Map<QualityStatus, Long>> getQualityCheckStatistics() {
//         Map<QualityStatus, Long> statistics = qualityCheckService.getQualityCheckStatistics();
//         return ResponseEntity.ok(statistics);
//     }
    
//     @PostMapping
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<QualityCheckDTO> createQualityCheck(@Valid @RequestBody CreateQualityCheckDTO createDTO) {
//         try {
//             QualityCheckDTO createdQualityCheck = qualityCheckService.createQualityCheck(createDTO);
//             return ResponseEntity.status(HttpStatus.CREATED).body(createdQualityCheck);
//         } catch (EntityNotFoundException e) {
//             return ResponseEntity.badRequest().build();
//         }
//     }
    
//     @PutMapping("/{id}")
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<QualityCheckDTO> updateQualityCheck(
//             @PathVariable Long id, 
//             @Valid @RequestBody CreateQualityCheckDTO updateDTO) {
//         try {
//             QualityCheckDTO updatedQualityCheck = qualityCheckService.updateQualityCheck(id, updateDTO);
//             return ResponseEntity.ok(updatedQualityCheck);
//         } catch (EntityNotFoundException e) {
//             return ResponseEntity.notFound().build();
//         }
//     }
    
//     @PatchMapping("/{id}/status")
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<QualityCheckDTO> updateQualityCheckStatus(
//             @PathVariable Long id, 
//             @RequestParam QualityStatus status) {
//         try {
//             QualityCheckDTO updatedQualityCheck = qualityCheckService.updateQualityCheckStatus(id, status);
//             return ResponseEntity.ok(updatedQualityCheck);
//         } catch (EntityNotFoundException e) {
//             return ResponseEntity.notFound().build();
//         }
//     }
    
//     @DeleteMapping("/{id}")
//     @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
//     public ResponseEntity<Void> deleteQualityCheck(@PathVariable Long id) {
//         try {
//             qualityCheckService.deleteQualityCheck(id);
//             return ResponseEntity.noContent().build();
//         } catch (EntityNotFoundException e) {
//             return ResponseEntity.notFound().build();
//         }
//     }
    
//     @ExceptionHandler(EntityNotFoundException.class)
//     public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException e) {
//         Map<String, String> error = new HashMap<>();
//         error.put("error", e.getMessage());
//         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//     }
    
//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
//         Map<String, String> errors = new HashMap<>();
//         e.getBindingResult().getAllErrors().forEach((error) -> {
//             String fieldName = ((FieldError) error).getField();
//             String errorMessage = error.getDefaultMessage();
//             errors.put(fieldName, errorMessage);
//         });
//         return ResponseEntity.badRequest().body(errors);
//     }
// }


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maison.vinitrackpro.dto.CreateQualityCheckDTO;
import com.maison.vinitrackpro.dto.ProductionBatchDTO;
import com.maison.vinitrackpro.dto.QualityCheckDTO;
import com.maison.vinitrackpro.model.QualityCheckType;
import com.maison.vinitrackpro.model.QualityStatus;
import com.maison.vinitrackpro.service.ProductionBatchService;
import com.maison.vinitrackpro.service.QualityCheckService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quality-checks")
// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@CrossOrigin(
    origins = {"http://localhost:5173", "http://localhost:3000"},
    allowCredentials = "true"
)
public class QualityCheckController {
    
    private final QualityCheckService qualityCheckService;

    private final ProductionBatchService productionBatchService;

    
    // public QualityCheckController(QualityCheckService qualityCheckService) {
    //     this.qualityCheckService = qualityCheckService;
    // }

    public QualityCheckController(QualityCheckService qualityCheckService, ProductionBatchService productionBatchService) {
        this.qualityCheckService = qualityCheckService;
        this.productionBatchService = productionBatchService;
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<List<QualityCheckDTO>> getAllQualityChecks() {
        List<QualityCheckDTO> qualityChecks = qualityCheckService.getAllQualityChecks();
        return ResponseEntity.ok(qualityChecks);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<QualityCheckDTO> getQualityCheckById(@PathVariable Long id) {
        Optional<QualityCheckDTO> qualityCheck = qualityCheckService.getQualityCheckById(id);
        return qualityCheck.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/batch/{batchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByBatch(@PathVariable Long batchId) {
        List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByBatchId(batchId);
        return ResponseEntity.ok(qualityChecks);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByStatus(@PathVariable QualityStatus status) {
        List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByStatus(status);
        return ResponseEntity.ok(qualityChecks);
    }
    
    @GetMapping("/status/{status}/paginated")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<Page<QualityCheckDTO>> getQualityChecksByStatusPaginated(
            @PathVariable QualityStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "checkDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
            Sort.by(sortBy).descending() :
            Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByStatus(status, pageable);
        return ResponseEntity.ok(qualityChecks);
    }
    
    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByType(@PathVariable QualityCheckType type) {
        List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByType(type);
        return ResponseEntity.ok(qualityChecks);
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByUser(@PathVariable Long userId) {
        List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByUser(userId);
        return ResponseEntity.ok(qualityChecks);
    }
    
    @GetMapping("/batch-number/{batchNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByBatchNumber(@PathVariable String batchNumber) {
        List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByBatchNumber(batchNumber);
        return ResponseEntity.ok(qualityChecks);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<List<QualityCheckDTO>> searchQualityChecksByName(@RequestParam String name) {
        List<QualityCheckDTO> qualityChecks = qualityCheckService.searchQualityChecksByName(name);
        return ResponseEntity.ok(qualityChecks);
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByDateRange(startDate, endDate);
        return ResponseEntity.ok(qualityChecks);
    }
    
    @GetMapping("/batch/{batchId}/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByBatchAndStatus(
            @PathVariable Long batchId, 
            @PathVariable QualityStatus status) {
        List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByBatchIdAndStatus(batchId, status);
        return ResponseEntity.ok(qualityChecks);
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<QualityStatus, Long>> getQualityCheckStatistics() {
        Map<QualityStatus, Long> statistics = qualityCheckService.getQualityCheckStatistics();
        return ResponseEntity.ok(statistics);
    }

    // Add this endpoint
@GetMapping("/batches")
@PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
public ResponseEntity<List<ProductionBatchDTO>> getAvailableBatches() {
    List<ProductionBatchDTO> batches = productionBatchService.getAllBatches();
    return ResponseEntity.ok(batches);
}
    
    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    // public ResponseEntity<QualityCheckDTO> createQualityCheck(@Valid @RequestBody CreateQualityCheckDTO createDTO) {
    //     try {
    //         QualityCheckDTO createdQualityCheck = qualityCheckService.createQualityCheck(createDTO);
    //         return ResponseEntity.status(HttpStatus.CREATED).body(createdQualityCheck);
    //     } catch (EntityNotFoundException e) {
    //         return ResponseEntity.badRequest().build();
    //     }
    // }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<?> createQualityCheck(@Valid @RequestBody CreateQualityCheckDTO createDTO) {
    try {
        System.out.println("Received DTO: " + createDTO);
        QualityCheckDTO createdQualityCheck = qualityCheckService.createQualityCheck(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQualityCheck);
    } catch (EntityNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    } catch (Exception e) {
        System.err.println("Unexpected error: " + e.getMessage());
        e.printStackTrace();
        Map<String, String> error = new HashMap<>();
        error.put("error", "An unexpected error occurred: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<QualityCheckDTO> updateQualityCheck(
            @PathVariable Long id, 
            @Valid @RequestBody CreateQualityCheckDTO updateDTO) {
        try {
            QualityCheckDTO updatedQualityCheck = qualityCheckService.updateQualityCheck(id, updateDTO);
            return ResponseEntity.ok(updatedQualityCheck);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<QualityCheckDTO> updateQualityCheckStatus(
            @PathVariable Long id, 
            @RequestParam QualityStatus status) {
        try {
            QualityCheckDTO updatedQualityCheck = qualityCheckService.updateQualityCheckStatus(id, status);
            return ResponseEntity.ok(updatedQualityCheck);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')")
    public ResponseEntity<Void> deleteQualityCheck(@PathVariable Long id) {
        try {
            qualityCheckService.deleteQualityCheck(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}


// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.FieldError;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PatchMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.maison.vinitrackpro.dto.CreateQualityCheckDTO;
// import com.maison.vinitrackpro.dto.QualityCheckDTO;
// import com.maison.vinitrackpro.model.QualityStatus;
// import com.maison.vinitrackpro.service.QualityCheckService;

// import jakarta.persistence.EntityNotFoundException;
// import jakarta.validation.Valid;

// @RestController
// @RequestMapping("/api/quality-checks")
// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
// public class QualityCheckController {
    
//     private final QualityCheckService qualityCheckService;
    
//     public QualityCheckController(QualityCheckService qualityCheckService) {
//         this.qualityCheckService = qualityCheckService;
//     }
    
//     @GetMapping
//     // @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')") // Temporarily disabled
//     public ResponseEntity<List<QualityCheckDTO>> getAllQualityChecks() {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.getAllQualityChecks();
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/{id}")
//     // @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')") // Temporarily disabled
//     public ResponseEntity<QualityCheckDTO> getQualityCheckById(@PathVariable Long id) {
//         Optional<QualityCheckDTO> qualityCheck = qualityCheckService.getQualityCheckById(id);
//         return qualityCheck.map(ResponseEntity::ok)
//                           .orElse(ResponseEntity.notFound().build());
//     }
    
//     @GetMapping("/batch/{batchId}")
//     // @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')") // Temporarily disabled
//     public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByBatch(@PathVariable Long batchId) {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByBatchId(batchId);
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/status/{status}")
//     // @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')") // Temporarily disabled
//     public ResponseEntity<List<QualityCheckDTO>> getQualityChecksByStatus(@PathVariable QualityStatus status) {
//         List<QualityCheckDTO> qualityChecks = qualityCheckService.getQualityChecksByStatus(status);
//         return ResponseEntity.ok(qualityChecks);
//     }
    
//     @GetMapping("/statistics")
//     // @PreAuthorize("hasRole('ADMIN')") // Temporarily disabled
//     public ResponseEntity<Map<String, Long>> getQualityCheckStatistics() {
//         Map<QualityStatus, Long> statistics = qualityCheckService.getQualityCheckStatistics();
//         // Convert enum keys to strings for frontend compatibility
//         Map<String, Long> stringKeyStats = new HashMap<>();
//         statistics.forEach((key, value) -> stringKeyStats.put(key.name(), value));
//         return ResponseEntity.ok(stringKeyStats);
//     }
    
//     @PostMapping
//     // @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')") // Temporarily disabled
//     public ResponseEntity<?> createQualityCheck(@Valid @RequestBody CreateQualityCheckDTO createDTO) {
//         try {
//             System.out.println("Received create request: " + createDTO.getName()); // Debug log
//             QualityCheckDTO createdQualityCheck = qualityCheckService.createQualityCheck(createDTO);
//             return ResponseEntity.status(HttpStatus.CREATED).body(createdQualityCheck);
//         } catch (EntityNotFoundException e) {
//             Map<String, String> error = new HashMap<>();
//             error.put("error", e.getMessage());
//             return ResponseEntity.badRequest().body(error);
//         } catch (Exception e) {
//             Map<String, String> error = new HashMap<>();
//             error.put("error", "Failed to create quality check: " + e.getMessage());
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//         }
//     }
    
//     @PutMapping("/{id}")
//     // @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')") // Temporarily disabled
//     public ResponseEntity<?> updateQualityCheck(
//             @PathVariable Long id, 
//             @Valid @RequestBody CreateQualityCheckDTO updateDTO) {
//         try {
//             QualityCheckDTO updatedQualityCheck = qualityCheckService.updateQualityCheck(id, updateDTO);
//             return ResponseEntity.ok(updatedQualityCheck);
//         } catch (EntityNotFoundException e) {
//             Map<String, String> error = new HashMap<>();
//             error.put("error", e.getMessage());
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//         }
//     }
    
//     @PatchMapping("/{id}/status")
//     // @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')") // Temporarily disabled
//     public ResponseEntity<?> updateQualityCheckStatus(
//             @PathVariable Long id, 
//             @RequestParam String status) { // Changed to String to handle conversion
//         try {
//             QualityStatus qualityStatus = QualityStatus.valueOf(status.toUpperCase());
//             QualityCheckDTO updatedQualityCheck = qualityCheckService.updateQualityCheckStatus(id, qualityStatus);
//             return ResponseEntity.ok(updatedQualityCheck);
//         } catch (IllegalArgumentException e) {
//             Map<String, String> error = new HashMap<>();
//             error.put("error", "Invalid status: " + status);
//             return ResponseEntity.badRequest().body(error);
//         } catch (EntityNotFoundException e) {
//             Map<String, String> error = new HashMap<>();
//             error.put("error", e.getMessage());
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//         }
//     }
    
//     @DeleteMapping("/{id}")
//     // @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY')") // Temporarily disabled
//     public ResponseEntity<?> deleteQualityCheck(@PathVariable Long id) {
//         try {
//             qualityCheckService.deleteQualityCheck(id);
//             return ResponseEntity.noContent().build();
//         } catch (EntityNotFoundException e) {
//             Map<String, String> error = new HashMap<>();
//             error.put("error", e.getMessage());
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//         }
//     }
    
//     @ExceptionHandler(EntityNotFoundException.class)
//     public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException e) {
//         Map<String, String> error = new HashMap<>();
//         error.put("error", e.getMessage());
//         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//     }
    
//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
//         Map<String, String> errors = new HashMap<>();
//         e.getBindingResult().getAllErrors().forEach((error) -> {
//             String fieldName = ((FieldError) error).getField();
//             String errorMessage = error.getDefaultMessage();
//             errors.put(fieldName, errorMessage);
//         });
//         return ResponseEntity.badRequest().body(errors);
//     }
// }