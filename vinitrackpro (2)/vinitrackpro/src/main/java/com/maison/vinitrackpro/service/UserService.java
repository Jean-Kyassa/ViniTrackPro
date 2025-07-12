package com.maison.vinitrackpro.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.dto.SignupRequest;
import com.maison.vinitrackpro.dto.UserDTO;
import com.maison.vinitrackpro.model.ERole;
import com.maison.vinitrackpro.model.Role;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.repository.RoleRepository;
import com.maison.vinitrackpro.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

     @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDTO);
    }
    
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDTO);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public UserDTO createUser(SignupRequest signupRequest) {
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "inventory":
                        Role inventoryRole = roleRepository.findByName(ERole.ROLE_INVENTORY)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(inventoryRole);
                        break;
                    case "logistics":
                        Role logisticsRole = roleRepository.findByName(ERole.ROLE_LOGISTICS)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(logisticsRole);
                        break;
                    case "quality":
                        Role qualityRole = roleRepository.findByName(ERole.ROLE_QUALITY)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(qualityRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    private UserDTO convertToDTO(User user) {
        Set<ERole> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roleNames
        );
    }
}
