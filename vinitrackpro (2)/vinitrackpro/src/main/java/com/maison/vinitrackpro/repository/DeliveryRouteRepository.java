package com.maison.vinitrackpro.repository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.DeliveryRoute;

@Repository
public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, Long> {
    
    List<DeliveryRoute> findByDriverId(Long driverId);
    
    List<DeliveryRoute> findByVehicleId(Long vehicleId);
    
    List<DeliveryRoute> findByDate(LocalDate date);
    
    List<DeliveryRoute> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT dr FROM DeliveryRoute dr WHERE dr.date = :date AND dr.driver.id = :driverId")
    List<DeliveryRoute> findByDateAndDriverId(@Param("date") LocalDate date, @Param("driverId") Long driverId);
    
    @Query("SELECT dr FROM DeliveryRoute dr WHERE dr.totalLoad > :maxLoad")
    List<DeliveryRoute> findOverloadedRoutes(@Param("maxLoad") double maxLoad);
    
    @Query("SELECT dr FROM DeliveryRoute dr WHERE dr.totalDistance > :maxDistance")
    List<DeliveryRoute> findLongDistanceRoutes(@Param("maxDistance") double maxDistance);
    
    List<DeliveryRoute> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT COUNT(dr) FROM DeliveryRoute dr WHERE dr.date = :date")
    long countByDate(@Param("date") LocalDate date);
}
