package com.maison.vinitrackpro.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.dto.CreateDeliveryRouteDTO;
import com.maison.vinitrackpro.dto.DeliveryRouteDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.DeliveryRoute;
import com.maison.vinitrackpro.model.Driver;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.model.Vehicle;
import com.maison.vinitrackpro.repository.DeliveryRouteRepository;
import com.maison.vinitrackpro.repository.DriverRepository;
import com.maison.vinitrackpro.repository.UserRepository;
import com.maison.vinitrackpro.repository.VehicleRepository;

@Service
@Transactional
public class DeliveryRouteService {

    @Autowired
    private DeliveryRouteRepository deliveryRouteRepository;
    
    @Autowired
    private DriverRepository driverRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private UserRepository userRepository;

    public DeliveryRouteDTO createRoute(CreateDeliveryRouteDTO createDTO, Long userId) {
        // Validate driver exists
        Driver driver = driverRepository.findById(createDTO.getDriverId())
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + createDTO.getDriverId()));
        
        // Validate vehicle exists
        Vehicle vehicle = vehicleRepository.findById(createDTO.getVehicleId())
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + createDTO.getVehicleId()));
        
        // Validate user exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        DeliveryRoute route = DeliveryRoute.builder()
            .name(createDTO.getName())
            .driver(driver)
            .vehicle(vehicle)
            .date(createDTO.getDate())
            .totalDistance(createDTO.getTotalDistance())
            .estimatedDuration(createDTO.getEstimatedDuration())
            .totalStops(createDTO.getTotalStops())
            .totalLoad(createDTO.getTotalLoad())
            .createdBy(user)
            .createdAt(LocalDateTime.now())
            .build();

        DeliveryRoute savedRoute = deliveryRouteRepository.save(route);
        return convertToDTO(savedRoute);
    }

    @Transactional(readOnly = true)
    public DeliveryRouteDTO getRouteById(Long id) {
        DeliveryRoute route = deliveryRouteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Delivery route not found with id: " + id));
        return convertToDTO(route);
    }

    @Transactional(readOnly = true)
    public List<DeliveryRouteDTO> getAllRoutes() {
        return deliveryRouteRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeliveryRouteDTO> getRoutesByDriver(Long driverId) {
        return deliveryRouteRepository.findByDriverId(driverId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public DeliveryRouteDTO updateRoute(Long id, CreateDeliveryRouteDTO updateDTO) {
        DeliveryRoute existingRoute = deliveryRouteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Delivery route not found with id: " + id));

        // Validate driver exists
        Driver driver = driverRepository.findById(updateDTO.getDriverId())
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + updateDTO.getDriverId()));
        
        // Validate vehicle exists
        Vehicle vehicle = vehicleRepository.findById(updateDTO.getVehicleId())
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + updateDTO.getVehicleId()));

        existingRoute.setName(updateDTO.getName());
        existingRoute.setDriver(driver);
        existingRoute.setVehicle(vehicle);
        existingRoute.setDate(updateDTO.getDate());
        existingRoute.setTotalDistance(updateDTO.getTotalDistance());
        existingRoute.setEstimatedDuration(updateDTO.getEstimatedDuration());
        existingRoute.setTotalStops(updateDTO.getTotalStops());
        existingRoute.setTotalLoad(updateDTO.getTotalLoad());

        DeliveryRoute updatedRoute = deliveryRouteRepository.save(existingRoute);
        return convertToDTO(updatedRoute);
    }

    public void deleteRoute(Long id) {
        if (!deliveryRouteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Delivery route not found with id: " + id);
        }
        deliveryRouteRepository.deleteById(id);
    }

    private DeliveryRouteDTO convertToDTO(DeliveryRoute route) {
        return DeliveryRouteDTO.builder()
            .id(route.getId())
            .name(route.getName())
            .driverId(route.getDriver().getId())
            .driverName(route.getDriver().getName())
            .vehicleId(route.getVehicle().getId())
            .vehicleRegistrationNumber(route.getVehicle().getRegistrationNumber())
            .date(route.getDate())
            .totalDistance(route.getTotalDistance())
            .estimatedDuration(route.getEstimatedDuration())
            .totalStops(route.getTotalStops())
            .totalLoad(route.getTotalLoad())
            .createdById(route.getCreatedBy().getId())
            .createdByName(route.getCreatedBy().getUsername())
            .createdAt(route.getCreatedAt())
            .build();
    }
}
