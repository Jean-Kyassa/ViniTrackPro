package com.matunda.vinitrackpro.repository;
import com.matunda.vinitrackpro.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    
    List<Vehicle> findByActive(boolean active);
    
    List<Vehicle> findByStatus(String status);
    
    boolean existsByRegistrationNumber(String registrationNumber);

    List<Vehicle> findByType(String type);
}

