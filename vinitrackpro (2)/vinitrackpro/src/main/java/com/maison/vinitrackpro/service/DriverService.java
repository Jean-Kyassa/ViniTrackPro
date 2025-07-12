package com.maison.vinitrackpro.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.dto.CreateDriverDTO;
import com.maison.vinitrackpro.dto.DriverDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.Driver;
import com.maison.vinitrackpro.model.DriverStatus;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.model.Vehicle;
import com.maison.vinitrackpro.repository.DriverRepository;
import com.maison.vinitrackpro.repository.UserRepository;
import com.maison.vinitrackpro.repository.VehicleRepository;

import jakarta.validation.ValidationException;

@Service
@Transactional
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private UserRepository userRepository;

    public DriverDTO createDriver(CreateDriverDTO createDTO, Long userId) {
        // Validate license number uniqueness
        if (driverRepository.existsByLicenseNumber(createDTO.getLicenseNumber())) {
            throw new ValidationException("License number already exists: " + createDTO.getLicenseNumber());
        }
        
        // Validate email uniqueness
        if (driverRepository.existsByEmail(createDTO.getEmail())) {
            throw new ValidationException("Email already exists: " + createDTO.getEmail());
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Driver driver = Driver.builder()
            .name(createDTO.getName())
            .licenseNumber(createDTO.getLicenseNumber())
            .phone(createDTO.getPhone())
            .email(createDTO.getEmail())
            .status(createDTO.getStatus())
            .rating(createDTO.getRating())
            .createdBy(user)
            .createdAt(LocalDateTime.now())
            .build();

        // Assign vehicle if provided
        if (createDTO.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(createDTO.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + createDTO.getVehicleId()));
            driver.setVehicle(vehicle);
        }

        Driver savedDriver = driverRepository.save(driver);
        return convertToDTO(savedDriver);
    }

    @Transactional(readOnly = true)
    public DriverDTO getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
        return convertToDTO(driver);
    }

    @Transactional(readOnly = true)
    public List<DriverDTO> getAllDrivers() {
        return driverRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DriverDTO> getDriversByStatus(DriverStatus status) {
        return driverRepository.findByStatus(status).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DriverDTO> getAvailableDrivers() {
        return driverRepository.findAvailableDrivers().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public DriverDTO updateDriver(Long id, CreateDriverDTO updateDTO) {
        Driver existingDriver = driverRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));

        // Validate license number uniqueness (excluding current driver)
        if (!existingDriver.getLicenseNumber().equals(updateDTO.getLicenseNumber()) &&
            driverRepository.existsByLicenseNumber(updateDTO.getLicenseNumber())) {
            throw new ValidationException("License number already exists: " + updateDTO.getLicenseNumber());
        }

        existingDriver.setName(updateDTO.getName());
        existingDriver.setLicenseNumber(updateDTO.getLicenseNumber());
        existingDriver.setPhone(updateDTO.getPhone());
        existingDriver.setEmail(updateDTO.getEmail());
        existingDriver.setStatus(updateDTO.getStatus());
        existingDriver.setRating(updateDTO.getRating());

        // Update vehicle assignment
        if (updateDTO.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(updateDTO.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + updateDTO.getVehicleId()));
            existingDriver.setVehicle(vehicle);
        } else {
            existingDriver.setVehicle(null);
        }

        Driver updatedDriver = driverRepository.save(existingDriver);
        return convertToDTO(updatedDriver);
    }

    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Driver not found with id: " + id);
        }
        driverRepository.deleteById(id);
    }

    private DriverDTO convertToDTO(Driver driver) {
        return DriverDTO.builder()
            .id(driver.getId())
            .name(driver.getName())
            .licenseNumber(driver.getLicenseNumber())
            .phone(driver.getPhone())
            .email(driver.getEmail())
            .status(driver.getStatus())
            .rating(driver.getRating())
            .vehicleId(driver.getVehicle() != null ? driver.getVehicle().getId() : null)
            .vehicleRegistrationNumber(driver.getVehicle() != null ? driver.getVehicle().getRegistrationNumber() : null)
            .currentRouteId(driver.getCurrentRoute() != null ? driver.getCurrentRoute().getId() : null)
            .currentRouteName(driver.getCurrentRoute() != null ? driver.getCurrentRoute().getName() : null)
            .createdById(driver.getCreatedBy().getId())
            .createdByName(driver.getCreatedBy().getUsername())
            .createdAt(driver.getCreatedAt())
            .build();
    }
}
