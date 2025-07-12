package com.maison.vinitrackpro.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.Driver;
import com.maison.vinitrackpro.model.DriverStatus;

@Repository
public interface DriverRepository  extends JpaRepository<Driver, Long> {
    
    Optional<Driver> findByLicenseNumber(String licenseNumber);
    
    List<Driver> findByStatus(DriverStatus status);
    
    List<Driver> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT d FROM Driver d WHERE d.rating >= :minRating")
    List<Driver> findByRatingGreaterThanEqual(@Param("minRating") double minRating);
    
    @Query("SELECT d FROM Driver d WHERE d.vehicle.id = :vehicleId")
    Optional<Driver> findByVehicleId(@Param("vehicleId") Long vehicleId);
    
    @Query("SELECT d FROM Driver d WHERE d.currentRoute.id = :routeId")
    List<Driver> findByCurrentRouteId(@Param("routeId") Long routeId);
    
    boolean existsByLicenseNumber(String licenseNumber);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT d FROM Driver d WHERE d.status = 'ACTIVE' AND d.vehicle IS NOT NULL")
    List<Driver> findAvailableDrivers();
}
