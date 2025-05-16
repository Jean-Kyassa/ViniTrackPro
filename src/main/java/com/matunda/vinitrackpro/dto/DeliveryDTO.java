package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {

    private Long id;
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    private String orderNumber;
    
    private String deliveryNumber;

    private String deliveryNotes;
    
    private String deliveryStatus;

    private String deliveryType;

    private LocalDateTime deliveredTime;
    
    private LocalDateTime scheduledDate;
    
    private LocalDateTime actualDeliveryDate;
    
    private Long vehicleId;
    
    private String vehicleRegistration;

    private String recipientSignatureUrl;

    private String routeInformation;
    
    private Long driverId;
    
    private String driverName;

    private String proofOfDeliveryUrl;

    private String recipientName;
    
    private String shippingAddress;
    
    private String trackingNumber;

    private LocalDateTime startTime;
    
    private String notes;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
