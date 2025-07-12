package com.maison.vinitrackpro.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.dto.CreateVehicleDTO;
import com.maison.vinitrackpro.dto.VehicleDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.model.Vehicle;
import com.maison.vinitrackpro.model.VehicleType;
import com.maison.vinitrackpro.repository.UserRepository;
import com.maison.vinitrackpro.repository.VehicleRepository;

import jakarta.validation.ValidationException;

@Service
@Transactional
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private UserRepository userRepository;

    public VehicleDTO createVehicle(CreateVehicleDTO createDTO, Long userId) {
        // Validate registration number uniqueness
        if (vehicleRepository.existsByRegistrationNumber(createDTO.getRegistrationNumber())) {
            throw new ValidationException("Registration number already exists: " + createDTO.getRegistrationNumber());
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Vehicle vehicle = Vehicle.builder()
            .registrationNumber(createDTO.getRegistrationNumber())
            .make(createDTO.getMake())
            .model(createDTO.getModel())
            .type(createDTO.getType())
            .capacity(createDTO.getCapacity())
            .lastMaintenanceDate(createDTO.getLastMaintenanceDate())
            .createdBy(user)
            .createdAt(LocalDateTime.now())
            .build();

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(savedVehicle);
    }

    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return convertToDTO(vehicle);
    }

    @Transactional(readOnly = true)
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByType(VehicleType type) {
        return vehicleRepository.findByType(type).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VehicleDTO> getUnassignedVehicles() {
        return vehicleRepository.findUnassignedVehicles().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public VehicleDTO updateVehicle(Long id, CreateVehicleDTO updateDTO) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        // Validate registration number uniqueness (excluding current vehicle)
        if (!existingVehicle.getRegistrationNumber().equals(updateDTO.getRegistrationNumber()) &&
            vehicleRepository.existsByRegistrationNumber(updateDTO.getRegistrationNumber())) {
            throw new ValidationException("Registration number already exists: " + updateDTO.getRegistrationNumber());
        }

        existingVehicle.setRegistrationNumber(updateDTO.getRegistrationNumber());
        existingVehicle.setMake(updateDTO.getMake());
        existingVehicle.setModel(updateDTO.getModel());
        existingVehicle.setType(updateDTO.getType());
        existingVehicle.setCapacity(updateDTO.getCapacity());
        existingVehicle.setLastMaintenanceDate(updateDTO.getLastMaintenanceDate());

        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        return convertToDTO(updatedVehicle);
    }

    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        return VehicleDTO.builder()
            .id(vehicle.getId())
            .registrationNumber(vehicle.getRegistrationNumber())
            .make(vehicle.getMake())
            .model(vehicle.getModel())
            .type(vehicle.getType())
            .capacity(vehicle.getCapacity())
            .lastMaintenanceDate(vehicle.getLastMaintenanceDate())
            .createdById(vehicle.getCreatedBy().getId())
            .createdByName(vehicle.getCreatedBy().getUsername())
            .createdAt(vehicle.getCreatedAt())
            .build();
    }
}
