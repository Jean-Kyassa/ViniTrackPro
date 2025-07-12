// package com.maison.vinitrackpro.controller;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.maison.vinitrackpro.dto.CreateVehicleDTO;
// import com.maison.vinitrackpro.dto.UserDTO;
// import com.maison.vinitrackpro.dto.VehicleDTO;
// import com.maison.vinitrackpro.model.VehicleType;
// import com.maison.vinitrackpro.service.UserService;
// import com.maison.vinitrackpro.service.VehicleService;

// import jakarta.validation.Valid;

// @RestController
// @RequestMapping("/api/vehicles")
// // @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
// @CrossOrigin(
//     origins = {"http://localhost:5173", "http://localhost:3000"},
//     allowCredentials = "true"
// )
// public class VehicleController {

//     @Autowired
//     private VehicleService vehicleService;
    
//     @Autowired
//     private UserService userService;

//     @PostMapping
//     public ResponseEntity<VehicleDTO> createVehicle(
//             @Valid @RequestBody CreateVehicleDTO createDTO,
//             Authentication authentication) {
//        Long userId = userService.getUserById(Long.parseLong(authentication.getName()))
//                                  .map(UserDTO::getId)
//                                  .orElseThrow(() -> new IllegalArgumentException("User not found"));
//         VehicleDTO createdVehicle = vehicleService.createVehicle(createDTO, userId);
//         return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
//         VehicleDTO vehicle = vehicleService.getVehicleById(id);
//         return ResponseEntity.ok(vehicle);
//     }

//     @GetMapping
//     public ResponseEntity<List<VehicleDTO>> getAllVehicles(
//             @RequestParam(required = false) VehicleType type,
//             @RequestParam(required = false, defaultValue = "false") boolean unassignedOnly) {
//         List<VehicleDTO> vehicles;
        
//         if (unassignedOnly) {
//             vehicles = vehicleService.getUnassignedVehicles();
//         } else if (type != null) {
//             vehicles = vehicleService.getVehiclesByType(type);
//         } else {
//             vehicles = vehicleService.getAllVehicles();
//         }
        
//         return ResponseEntity.ok(vehicles);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<VehicleDTO> updateVehicle(
//             @PathVariable Long id,
//             @Valid @RequestBody CreateVehicleDTO updateDTO) {
//         VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, updateDTO);
//         return ResponseEntity.ok(updatedVehicle);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
//         vehicleService.deleteVehicle(id);
//         return ResponseEntity.noContent().build();
//     }
// }


package com.maison.vinitrackpro.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maison.vinitrackpro.dto.CreateVehicleDTO;
import com.maison.vinitrackpro.dto.UserDTO;
import com.maison.vinitrackpro.dto.VehicleDTO;
import com.maison.vinitrackpro.model.VehicleType;
import com.maison.vinitrackpro.service.UserService;
import com.maison.vinitrackpro.service.VehicleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(
    origins = {"http://localhost:5173", "http://localhost:3000"},
    allowCredentials = "true")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<VehicleDTO> createVehicle(
            @Valid @RequestBody CreateVehicleDTO createDTO,
            Authentication authentication) {
        
        // Get user by username instead of trying to parse as Long
        Long userId = userService.getUserByUsername(authentication.getName())
                                .map(UserDTO::getId)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        VehicleDTO createdVehicle = vehicleService.createVehicle(createDTO, userId);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(
            @RequestParam(required = false) VehicleType type,
            @RequestParam(required = false, defaultValue = "false") boolean unassignedOnly) {
        List<VehicleDTO> vehicles;
        
        if (unassignedOnly) {
            vehicles = vehicleService.getUnassignedVehicles();
        } else if (type != null) {
            vehicles = vehicleService.getVehiclesByType(type);
        } else {
            vehicles = vehicleService.getAllVehicles();
        }
        
        return ResponseEntity.ok(vehicles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody CreateVehicleDTO updateDTO) {
        VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, updateDTO);
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
