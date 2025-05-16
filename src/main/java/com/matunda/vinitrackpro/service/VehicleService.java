package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.VehicleDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Vehicle;
import com.matunda.vinitrackpro.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleDTO> getActiveVehicles() {
        return vehicleRepository.findByActive(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return convertToDTO(vehicle);
    }
    public List<VehicleDTO> getVehiclesByType(String type) {
        return vehicleRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public VehicleDTO getVehicleByRegistrationNumber(String registrationNumber) {
        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with registration number: " + registrationNumber));
        return convertToDTO(vehicle);
    }

    public List<VehicleDTO> getVehiclesByStatus(String status) {
        return vehicleRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        if (vehicleRepository.existsByRegistrationNumber(vehicleDTO.getRegistrationNumber())) {
            throw new IllegalArgumentException("Vehicle with registration number " + vehicleDTO.getRegistrationNumber() + " already exists");
        }

        Vehicle vehicle = Vehicle.builder()
                .registrationNumber(vehicleDTO.getRegistrationNumber())
                .model(vehicleDTO.getModel())
                .type(vehicleDTO.getVehicleType())
                .capacity(vehicleDTO.getCapacity() != null ? Integer.parseInt(vehicleDTO.getCapacity()) : null)
                .lastMaintenanceDate(vehicleDTO.getLastMaintenanceDate() != null ? vehicleDTO.getLastMaintenanceDate().toLocalDate() : null)
                .nextMaintenanceDate(vehicleDTO.getNextMaintenanceDate() != null ? vehicleDTO.getNextMaintenanceDate().toLocalDate() : null)
                .status(Vehicle.VehicleStatus.valueOf(vehicleDTO.getStatus()))
                .active(vehicleDTO.isActive())
                .build();

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(savedVehicle);
    }

    @Transactional
    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        if (!vehicle.getRegistrationNumber().equals(vehicleDTO.getRegistrationNumber()) &&
                vehicleRepository.existsByRegistrationNumber(vehicleDTO.getRegistrationNumber())) {
            throw new IllegalArgumentException("Vehicle with registration number " + vehicleDTO.getRegistrationNumber() + " already exists");
        }

        vehicle.setRegistrationNumber(vehicleDTO.getRegistrationNumber());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setType(vehicleDTO.getVehicleType());
        vehicle.setCapacity(vehicleDTO.getCapacity() != null ? Integer.parseInt(vehicleDTO.getCapacity()) : null);
        vehicle.setLastMaintenanceDate(vehicleDTO.getLastMaintenanceDate() != null ? vehicleDTO.getLastMaintenanceDate().toLocalDate() : null);
        vehicle.setNextMaintenanceDate(vehicleDTO.getNextMaintenanceDate() != null ? vehicleDTO.getNextMaintenanceDate().toLocalDate() : null);
        vehicle.setStatus(Vehicle.VehicleStatus.valueOf(vehicleDTO.getStatus()));
        vehicle.setActive(vehicleDTO.isActive());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(updatedVehicle);
    }

    @Transactional
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setRegistrationNumber(vehicle.getRegistrationNumber());
        dto.setVehicleType(vehicle.getType());
        dto.setModel(vehicle.getModel());
        dto.setCapacity(vehicle.getCapacity() != null ? vehicle.getCapacity().toString() : null);
        dto.setStatus(vehicle.getStatus().name());
        dto.setLastMaintenanceDate(vehicle.getLastMaintenanceDate() != null ? vehicle.getLastMaintenanceDate().atStartOfDay() : null);
        dto.setNextMaintenanceDate(vehicle.getNextMaintenanceDate() != null ? vehicle.getNextMaintenanceDate().atStartOfDay() : null);
        dto.setActive(vehicle.isActive());
        dto.setCreatedAt(vehicle.getCreatedAt());
        dto.setUpdatedAt(vehicle.getUpdatedAt());
        return dto;
    }
}