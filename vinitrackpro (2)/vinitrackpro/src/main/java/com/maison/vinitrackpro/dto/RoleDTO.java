package com.maison.vinitrackpro.dto;

import com.maison.vinitrackpro.model.ERole;

public class RoleDTO {

     private Integer id;
    private ERole name;
    
    // Constructors
    public RoleDTO() {}
    
    public RoleDTO(Integer id, ERole name) {
        this.id = id;
        this.name = name;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public ERole getName() {
        return name;
    }
    
    public void setName(ERole name) {
        this.name = name;
    }
}
