//package com.matunda.vinitrackpro.dto;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import jakarta.validation.constraints.*;
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class PaymentDTO {
//
//     private Long id;
//
//    @NotNull(message = "Order ID is required")
//    private Long orderId;
//
//    private String orderNumber;
//
//    private String transactionId;
//
//    @NotNull(message = "Amount is required")
//    @Positive(message = "Amount must be positive")
//    private BigDecimal amount;
//
//    private String paymentMethod;
//
//    private String status;
//
//    private LocalDateTime paymentDate;
//
//    private String paymentDetails;
//
//    private String receiptUrl;
//
//    private String processedBy;
//
//    private String notes;
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//
//    public String getProcessedById() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}

package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long id;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    private String orderNumber;

    private String transactionId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String paymentMethod;

    private String status;

    private LocalDateTime paymentDate;

    private String paymentDetails;

    private String receiptUrl;

    private String processedBy;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Remove this method since it's not implemented and just throws an exception
    // public String getProcessedById() {
    //    throw new UnsupportedOperationException("Not supported yet.");
    // }
}