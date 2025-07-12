package com.maison.vinitrackpro.repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.DeliveryOrder;
import com.maison.vinitrackpro.model.OrderStatus;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryOrder, Long> {
    
    // Find by order number
    Optional<DeliveryOrder> findByOrderNumber(String orderNumber);
    
    // Find by customer
    List<DeliveryOrder> findByCustomerId(Long customerId);
    Page<DeliveryOrder> findByCustomerId(Long customerId, Pageable pageable);
    
    // Find by status
    List<DeliveryOrder> findByStatus(OrderStatus status);
    Page<DeliveryOrder> findByStatus(OrderStatus status, Pageable pageable);
    
    // Find by route
    List<DeliveryOrder> findByRouteId(Long routeId);
    
    // Find by date range
    @Query("SELECT d FROM DeliveryOrder d WHERE d.orderDate BETWEEN :startDate AND :endDate")
    List<DeliveryOrder> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT d FROM DeliveryOrder d WHERE d.deliveryDate BETWEEN :startDate AND :endDate")
    List<DeliveryOrder> findByDeliveryDateBetween(@Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate);
    
    // Find pending orders
    @Query("SELECT d FROM DeliveryOrder d WHERE d.status IN ('PENDING', 'PROCESSING')")
    List<DeliveryOrder> findPendingOrders();
    
    // Find orders by driver
    @Query("SELECT d FROM DeliveryOrder d WHERE d.route.driver.id = :driverId")
    List<DeliveryOrder> findByDriverId(@Param("driverId") Long driverId);
    
    // Find orders by vehicle
    @Query("SELECT d FROM DeliveryOrder d WHERE d.route.vehicle.id = :vehicleId")
    List<DeliveryOrder> findByVehicleId(@Param("vehicleId") Long vehicleId);
    
    // Count orders by status
    long countByStatus(OrderStatus status);
    
    // Find orders with total amount greater than
    List<DeliveryOrder> findByTotalAmountGreaterThan(double amount);
    
    // Find recent orders
    @Query("SELECT d FROM DeliveryOrder d WHERE d.orderDate >= :date ORDER BY d.orderDate DESC")
    List<DeliveryOrder> findRecentOrders(@Param("date") LocalDateTime date);
    
    // Search orders
   // Search orders
@Query("SELECT d FROM DeliveryOrder d WHERE " +
"LOWER(d.orderNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
"LOWER(d.deliveryAddress) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
"LOWER(d.customer.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
Page<DeliveryOrder> searchOrders(@Param("keyword") String keyword, Pageable pageable);

    
    // Find orders by status and date range
    @Query("SELECT d FROM DeliveryOrder d WHERE d.status = :status AND d.orderDate BETWEEN :startDate AND :endDate")
    List<DeliveryOrder> findByStatusAndDateRange(@Param("status") OrderStatus status,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);
}