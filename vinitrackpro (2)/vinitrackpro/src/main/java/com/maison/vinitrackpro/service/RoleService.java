package com.maison.vinitrackpro.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.maison.vinitrackpro.dto.RoleDTO;
import com.maison.vinitrackpro.model.ERole;
import com.maison.vinitrackpro.model.Role;
import com.maison.vinitrackpro.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<RoleDTO> getRoleById(Integer id) {
        return roleRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public Optional<RoleDTO> getRoleByName(ERole name) {
        return roleRepository.findByName(name)
                .map(this::convertToDTO);
    }
    
    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }
}
