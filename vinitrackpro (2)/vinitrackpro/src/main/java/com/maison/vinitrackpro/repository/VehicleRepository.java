package com.maison.vinitrackpro.repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.Vehicle;
import com.maison.vinitrackpro.model.VehicleType;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    
    List<Vehicle> findByType(VehicleType type);
    
    List<Vehicle> findByMakeContainingIgnoreCase(String make);
    
    @Query("SELECT v FROM Vehicle v WHERE v.capacity >= :minCapacity")
    List<Vehicle> findByCapacityGreaterThanEqual(@Param("minCapacity") double minCapacity);
    
    @Query("SELECT v FROM Vehicle v WHERE v.lastMaintenanceDate < :cutoffDate")
    List<Vehicle> findVehiclesDueForMaintenance(@Param("cutoffDate") LocalDate cutoffDate);
    
    @Query("SELECT v FROM Vehicle v WHERE v.id NOT IN (SELECT d.vehicle.id FROM Driver d WHERE d.vehicle IS NOT NULL)")
    List<Vehicle> findUnassignedVehicles();
    
    boolean existsByRegistrationNumber(String registrationNumber);
}
