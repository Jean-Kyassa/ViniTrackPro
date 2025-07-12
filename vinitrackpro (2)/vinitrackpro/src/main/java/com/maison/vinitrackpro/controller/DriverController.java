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

// import com.maison.vinitrackpro.dto.CreateDriverDTO;
// import com.maison.vinitrackpro.dto.DriverDTO;
// import com.maison.vinitrackpro.dto.UserDTO;
// import com.maison.vinitrackpro.model.DriverStatus;
// import com.maison.vinitrackpro.service.DriverService;
// import com.maison.vinitrackpro.service.UserService;

// import jakarta.validation.Valid;

// @RestController
// @RequestMapping("/api/drivers")
// // @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
// @CrossOrigin(
//     origins = {"http://localhost:5173", "http://localhost:3000"},
//     allowCredentials = "true"
// )
// public class DriverController {

//     @Autowired
//     private DriverService driverService;
    
//     @Autowired
//     private UserService userService;

//     @PostMapping
//     public ResponseEntity<DriverDTO> createDriver(
//             @Valid @RequestBody CreateDriverDTO createDTO,
//             Authentication authentication) {
//         Long userId = userService.getUserById(Long.parseLong(authentication.getName()))
//                                  .map(UserDTO::getId)
//                                  .orElseThrow(() -> new IllegalArgumentException("User not found"));
//         DriverDTO createdDriver = driverService.createDriver(createDTO, userId);
//         return new ResponseEntity<>(createdDriver, HttpStatus.CREATED);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<DriverDTO> getDriverById(@PathVariable Long id) {
//         DriverDTO driver = driverService.getDriverById(id);
//         return ResponseEntity.ok(driver);
//     }

//     @GetMapping
//     public ResponseEntity<List<DriverDTO>> getAllDrivers(
//             @RequestParam(required = false) DriverStatus status,
//             @RequestParam(required = false, defaultValue = "false") boolean availableOnly) {
//         List<DriverDTO> drivers;
        
//         if (availableOnly) {
//             drivers = driverService.getAvailableDrivers();
//         } else if (status != null) {
//             drivers = driverService.getDriversByStatus(status);
//         } else {
//             drivers = driverService.getAllDrivers();
//         }
        
//         return ResponseEntity.ok(drivers);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<DriverDTO> updateDriver(
//             @PathVariable Long id,
//             @Valid @RequestBody CreateDriverDTO updateDTO) {
//         DriverDTO updatedDriver = driverService.updateDriver(id, updateDTO);
//         return ResponseEntity.ok(updatedDriver);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
//         driverService.deleteDriver(id);
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

import com.maison.vinitrackpro.dto.CreateDriverDTO;
import com.maison.vinitrackpro.dto.DriverDTO;
import com.maison.vinitrackpro.dto.UserDTO;
import com.maison.vinitrackpro.model.DriverStatus;
import com.maison.vinitrackpro.service.DriverService;
import com.maison.vinitrackpro.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/drivers")
@CrossOrigin(
    origins = {"http://localhost:5173", "http://localhost:3000"},
    allowCredentials = "true")
public class DriverController {

    @Autowired
    private DriverService driverService;
    
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(
            @Valid @RequestBody CreateDriverDTO createDTO,
            Authentication authentication) {
        // Get user by username instead of trying to parse username as ID
        Long userId = userService.getUserByUsername(authentication.getName())
                                 .map(UserDTO::getId)
                                 .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        DriverDTO createdDriver = driverService.createDriver(createDTO, userId);
        return new ResponseEntity<>(createdDriver, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable Long id) {
        DriverDTO driver = driverService.getDriverById(id);
        return ResponseEntity.ok(driver);
    }

    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers(
            @RequestParam(required = false) DriverStatus status,
            @RequestParam(required = false, defaultValue = "false") boolean availableOnly) {
        List<DriverDTO> drivers;
        
        if (availableOnly) {
            drivers = driverService.getAvailableDrivers();
        } else if (status != null) {
            drivers = driverService.getDriversByStatus(status);
        } else {
            drivers = driverService.getAllDrivers();
        }
        
        return ResponseEntity.ok(drivers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(
            @PathVariable Long id,
            @Valid @RequestBody CreateDriverDTO updateDTO) {
        DriverDTO updatedDriver = driverService.updateDriver(id, updateDTO);
        return ResponseEntity.ok(updatedDriver);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }
}
