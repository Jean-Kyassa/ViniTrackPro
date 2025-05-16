package com.matunda.vinitrackpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;    
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters")
    private String password;
    
    @NotBlank(message = "FullName name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 50 characters")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email must be less than 50 characters")
    @Email(message = "Email should be valid")
    private String email;
    
    private String phone;
    
    private String language;
    
    private String position;
    
    private String department;
    
    private Set<String> roles;
    
    private boolean active;
    
    private LocalDateTime lastLogin;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
