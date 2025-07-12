package com.maison.vinitrackpro.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionBatchDTO {

    private Long id;
    private String batchNumber;
    private Long productId;
    private String productName;
    private String productCode;
    private int quantity;
    private LocalDate productionDate;
    private LocalDate expiryDate;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
    private List<QualityCheckDTO> qualityChecks;
 

}
