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

// import com.maison.vinitrackpro.dto.CreateDeliveryRouteDTO;
// import com.maison.vinitrackpro.dto.DeliveryRouteDTO;
// import com.maison.vinitrackpro.dto.UserDTO;
// import com.maison.vinitrackpro.service.DeliveryRouteService;
// import com.maison.vinitrackpro.service.UserService;

// import jakarta.validation.Valid;

// @RestController
// @RequestMapping("/api/delivery-routes")
// // @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
// @CrossOrigin(
//     origins = {"http://localhost:5173", "http://localhost:3000"},
//     allowCredentials = "true"
// )
// public class DeliveryRouteController {

//     @Autowired
//     private DeliveryRouteService deliveryRouteService;
    
//     @Autowired
//     private UserService userService;

//     @PostMapping
//     public ResponseEntity<DeliveryRouteDTO> createRoute(
//             @Valid @RequestBody CreateDeliveryRouteDTO createDTO,
//             Authentication authentication) {
//         Long userId = userService.getUserById(Long.parseLong(authentication.getName()))
//                                  .map(UserDTO::getId)
//                                  .orElseThrow(() -> new IllegalArgumentException("User not found"));
//         DeliveryRouteDTO createdRoute = deliveryRouteService.createRoute(createDTO, userId);
//         return new ResponseEntity<>(createdRoute, HttpStatus.CREATED);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<DeliveryRouteDTO> getRouteById(@PathVariable Long id) {
//         DeliveryRouteDTO route = deliveryRouteService.getRouteById(id);
//         return ResponseEntity.ok(route);
//     }

//     @GetMapping
//     public ResponseEntity<List<DeliveryRouteDTO>> getAllRoutes(
//             @RequestParam(required = false) Long driverId) {
//         List<DeliveryRouteDTO> routes;
        
//         if (driverId != null) {
//             routes = deliveryRouteService.getRoutesByDriver(driverId);
//         } else {
//             routes = deliveryRouteService.getAllRoutes();
//         }
        
//         return ResponseEntity.ok(routes);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<DeliveryRouteDTO> updateRoute(
//             @PathVariable Long id,
//             @Valid @RequestBody CreateDeliveryRouteDTO updateDTO) {
//         DeliveryRouteDTO updatedRoute = deliveryRouteService.updateRoute(id, updateDTO);
//         return ResponseEntity.ok(updatedRoute);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
//         deliveryRouteService.deleteRoute(id);
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

import com.maison.vinitrackpro.dto.CreateDeliveryRouteDTO;
import com.maison.vinitrackpro.dto.DeliveryRouteDTO;
import com.maison.vinitrackpro.dto.UserDTO;
import com.maison.vinitrackpro.service.DeliveryRouteService;
import com.maison.vinitrackpro.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/delivery-routes")
@CrossOrigin(
    origins = {"http://localhost:5173", "http://localhost:3000"},
    allowCredentials = "true")
public class DeliveryRouteController {

    @Autowired
    private DeliveryRouteService deliveryRouteService;
    
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<DeliveryRouteDTO> createRoute(
            @Valid @RequestBody CreateDeliveryRouteDTO createDTO,
            Authentication authentication) {
        // Get user by username instead of trying to parse username as ID
        Long userId = userService.getUserByUsername(authentication.getName())
                                 .map(UserDTO::getId)
                                 .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        DeliveryRouteDTO createdRoute = deliveryRouteService.createRoute(createDTO, userId);
        return new ResponseEntity<>(createdRoute, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryRouteDTO> getRouteById(@PathVariable Long id) {
        DeliveryRouteDTO route = deliveryRouteService.getRouteById(id);
        return ResponseEntity.ok(route);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryRouteDTO>> getAllRoutes(
            @RequestParam(required = false) Long driverId) {
        List<DeliveryRouteDTO> routes;
        
        if (driverId != null) {
            routes = deliveryRouteService.getRoutesByDriver(driverId);
        } else {
            routes = deliveryRouteService.getAllRoutes();
        }
        
        return ResponseEntity.ok(routes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryRouteDTO> updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody CreateDeliveryRouteDTO updateDTO) {
        DeliveryRouteDTO updatedRoute = deliveryRouteService.updateRoute(id, updateDTO);
        return ResponseEntity.ok(updatedRoute);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        deliveryRouteService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
