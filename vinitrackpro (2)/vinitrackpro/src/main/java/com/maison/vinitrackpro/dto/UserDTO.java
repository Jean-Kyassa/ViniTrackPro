package com.maison.vinitrackpro.dto;

import java.util.Set;

import com.maison.vinitrackpro.model.ERole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {

    private Long id;
    
    @NotBlank
    @Size(max = 20)
    private String username;
    
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    private Set<ERole> roles;
    
    // Constructors
    public UserDTO() {}
    
    public UserDTO(Long id, String username, String email, Set<ERole> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Set<ERole> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<ERole> roles) {
        this.roles = roles;
    }
}
