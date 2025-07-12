package com.maison.vinitrackpro.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maison.vinitrackpro.dto.LoginRequest;
import com.maison.vinitrackpro.dto.SignupRequest;
import com.maison.vinitrackpro.dto.UserDTO;
import com.maison.vinitrackpro.security.JwtUtils;
import com.maison.vinitrackpro.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
    "http://localhost:3000", 
    "http://localhost:3001", 
    "http://localhost:5173",
    "http://127.0.0.1:3000",
    "http://127.0.0.1:3001", 
    "http://127.0.0.1:5173"
}, allowCredentials = "true", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            Optional<UserDTO> userDTO = userService.getUserByUsername(loginRequest.getUsername());
            
            if (userDTO.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User signed in successfully!");
            response.put("user", userDTO.get());
            response.put("token", jwt);
            response.put("type", "Bearer");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    // ... rest of your methods remain the same
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        Map<String, String> response = new HashMap<>();
        
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            response.put("error", "Username is already taken!");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            response.put("error", "Email is already in use!");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            UserDTO userDTO = userService.createUser(signUpRequest);
            response.put("message", "User registered successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error occurred during registration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<UserDTO> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully!");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        Map<String, String> response = new HashMap<>();
        response.put("message", "User signed out successfully!");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        String username = authentication.getName();
        Optional<UserDTO> user = userService.getUserByUsername(username);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User profile not found");
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/user/profile")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserDTO userDTO,
                                              Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        String username = authentication.getName();
        Optional<UserDTO> currentUser = userService.getUserByUsername(username);
        
        if (currentUser.isPresent()) {
            try {
                UserDTO updatedUser = userService.updateUser(currentUser.get().getId(), userDTO);
                return ResponseEntity.ok(updatedUser);
            } catch (RuntimeException e) {
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                return ResponseEntity.badRequest().body(error);
            }
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User profile not found");
            return ResponseEntity.notFound().build();
        }
    }
}
