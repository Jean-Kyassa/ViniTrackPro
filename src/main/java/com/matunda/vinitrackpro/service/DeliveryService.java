package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.DeliveryDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Delivery;
import com.matunda.vinitrackpro.model.Order;
import com.matunda.vinitrackpro.model.User;
import com.matunda.vinitrackpro.model.Vehicle;
import com.matunda.vinitrackpro.repository.DeliveryRepository;
import com.matunda.vinitrackpro.repository.OrderRepository;
import com.matunda.vinitrackpro.repository.UserRepository;
import com.matunda.vinitrackpro.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final NotificationService notificationService;
    private final OrderService orderService;

    public List<DeliveryDTO> getAllDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DeliveryDTO getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
        return convertToDTO(delivery);
    }

    public DeliveryDTO getDeliveryByTrackingNumber(String trackingNumber) {
        Delivery delivery = deliveryRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with tracking number: " + trackingNumber));
        return convertToDTO(delivery);
    }

    public List<DeliveryDTO> getDeliveriesByStatus(String status) {
        Delivery.DeliveryStatus deliveryStatus = Delivery.DeliveryStatus.valueOf(status);
        return deliveryRepository.findByStatus(deliveryStatus).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DeliveryDTO> getDeliveriesByDriver(Long driverId) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + driverId));
        return deliveryRepository.findByDriver(driver).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DeliveryDTO getDeliveryByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        List<Delivery> deliveries = deliveryRepository.findByOrder(order);

        if (deliveries.isEmpty()) {
            throw new ResourceNotFoundException("No deliveries found for order id: " + orderId);
        }

        // Assuming one order has one delivery, get the first one
        return convertToDTO(deliveries.get(0));
    }

    @Transactional
    public DeliveryDTO assignDriver(Long deliveryId, Long driverId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + deliveryId));

        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + driverId));

        delivery.setDriver(driver);
        Delivery updatedDelivery = deliveryRepository.save(delivery);

        // Create notification
        String message = "Driver " + driver.getFullName() + " assigned to delivery " +
                delivery.getTrackingNumber();
        notificationService.createOrderNotification(delivery.getOrder(), message);

        return convertToDTO(updatedDelivery);
    }

    @Transactional
    public DeliveryDTO assignVehicle(Long deliveryId, Long vehicleId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + deliveryId));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

        delivery.setVehicle(vehicle);
        Delivery updatedDelivery = deliveryRepository.save(delivery);

        // Create notification
        String message = "Vehicle " + vehicle.getRegistrationNumber() + " assigned to delivery " +
                delivery.getTrackingNumber();
        notificationService.createOrderNotification(delivery.getOrder(), message);

        return convertToDTO(updatedDelivery);
    }
    @Transactional
    public DeliveryDTO createDelivery(DeliveryDTO deliveryDTO) {
        Order order = orderRepository.findById(deliveryDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + deliveryDTO.getOrderId()));
        
        User driver = null;
        if (deliveryDTO.getDriverId() != null) {
            driver = userRepository.findById(deliveryDTO.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + deliveryDTO.getDriverId()));
        }
        
        Vehicle vehicle = null;
        if (deliveryDTO.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(deliveryDTO.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + deliveryDTO.getVehicleId()));
        }

        Delivery delivery = new Delivery();
        delivery.setTrackingNumber(generateTrackingNumber());
        delivery.setOrder(order);
        delivery.setDriver(driver);
        delivery.setVehicle(vehicle);
        delivery.setScheduledDate(deliveryDTO.getScheduledDate());
        delivery.setStatus(Delivery.DeliveryStatus.valueOf(deliveryDTO.getDeliveryStatus()));
        delivery.setDeliveryNotes(deliveryDTO.getDeliveryNotes());
        delivery.setRouteInformation(deliveryDTO.getRouteInformation());

        Delivery savedDelivery = deliveryRepository.save(delivery);
        
        // Update order status to READY_FOR_DELIVERY if it's not already past that stage
        if (order.getStatus() == Order.OrderStatus.CONFIRMED || 
            order.getStatus() == Order.OrderStatus.PROCESSING) {
            orderService.updateOrderStatus(order.getId(), Order.OrderStatus.READY_FOR_DELIVERY.name());
        }
        
        // Create notification for new delivery
        String message = "New delivery scheduled for order " + order.getOrderNumber() + 
                         " on " + deliveryDTO.getScheduledDate();
        notificationService.createOrderNotification(order, message);
        
        return convertToDTO(savedDelivery);
    }

    @Transactional
    public DeliveryDTO updateDeliveryStatus(Long id, String status, String notes) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
        
        Delivery.DeliveryStatus newStatus = Delivery.DeliveryStatus.valueOf(status);
        Delivery.DeliveryStatus oldStatus = delivery.getStatus();
        
        delivery.setStatus(newStatus);
        
        if (notes != null && !notes.isEmpty()) {
            delivery.setDeliveryNotes(notes);
        }
        
        // Update timestamps based on status
        if (newStatus == Delivery.DeliveryStatus.IN_TRANSIT && oldStatus != Delivery.DeliveryStatus.IN_TRANSIT) {
            delivery.setStartTime(LocalDateTime.now());
        } else if (newStatus == Delivery.DeliveryStatus.DELIVERED && oldStatus != Delivery.DeliveryStatus.DELIVERED) {
            delivery.setDeliveredTime(LocalDateTime.now());
            
            // Update order status to DELIVERED
            orderService.updateOrderStatus(delivery.getOrder().getId(), Order.OrderStatus.DELIVERED.name());
        }
        
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        
        // Create notification for status change
        String message = "Delivery status changed from " + oldStatus + " to " + newStatus + 
                         " for order " + delivery.getOrder().getOrderNumber();
        notificationService.createOrderNotification(delivery.getOrder(), message);
        return convertToDTO(updatedDelivery);
    }

    @Transactional
    public DeliveryDTO updateDelivery(Long id, DeliveryDTO deliveryDTO) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
        
        User driver = null;
        if (deliveryDTO.getDriverId() != null) {
            driver = userRepository.findById(deliveryDTO.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + deliveryDTO.getDriverId()));
        }
        
        Vehicle vehicle = null;
        if (deliveryDTO.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(deliveryDTO.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + deliveryDTO.getVehicleId()));
        }

        delivery.setDriver(driver);
        delivery.setVehicle(vehicle);
        delivery.setScheduledDate(deliveryDTO.getScheduledDate());
        delivery.setStatus(Delivery.DeliveryStatus.valueOf(deliveryDTO.getDeliveryStatus()));
        delivery.setDeliveryNotes(deliveryDTO.getDeliveryNotes());
        delivery.setRecipientName(deliveryDTO.getRecipientName());
        delivery.setRecipientSignatureUrl(deliveryDTO.getRecipientSignatureUrl());
        delivery.setProofOfDeliveryUrl(deliveryDTO.getProofOfDeliveryUrl());
        delivery.setRouteInformation(deliveryDTO.getRouteInformation());

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        return convertToDTO(updatedDelivery);
    }

    @Transactional
    public void deleteDelivery(Long id) {
        if (!deliveryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Delivery not found with id: " + id);
        }
        deliveryRepository.deleteById(id);
    }

    private String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private DeliveryDTO convertToDTO(Delivery delivery) {
        DeliveryDTO dto = new DeliveryDTO();
        dto.setId(delivery.getId());
        dto.setTrackingNumber(delivery.getTrackingNumber());
        dto.setOrderId(delivery.getOrder().getId());
        dto.setOrderNumber(delivery.getOrder().getOrderNumber());
        
        if (delivery.getDriver() != null) {
            dto.setDriverId(delivery.getDriver().getId());
            dto.setDriverName(delivery.getDriver().getFullName());
        }
        
        if (delivery.getVehicle() != null) {
            dto.setVehicleId(delivery.getVehicle().getId());
            dto.setVehicleRegistration(delivery.getVehicle().getRegistrationNumber());
        }
        
        dto.setScheduledDate(delivery.getScheduledDate());
        dto.setStartTime(delivery.getStartTime());
        dto.setDeliveredTime(delivery.getDeliveredTime());
        dto.setDeliveryStatus(delivery.getStatus().name());
        dto.setDeliveryNotes(delivery.getDeliveryNotes());
        dto.setRecipientName(delivery.getRecipientName());
        dto.setRecipientSignatureUrl(delivery.getRecipientSignatureUrl());
        dto.setProofOfDeliveryUrl(delivery.getProofOfDeliveryUrl());
        dto.setRouteInformation(delivery.getRouteInformation());
        dto.setCreatedAt(delivery.getCreatedAt());
        dto.setUpdatedAt(delivery.getUpdatedAt());
        
        return dto;
    }
}
