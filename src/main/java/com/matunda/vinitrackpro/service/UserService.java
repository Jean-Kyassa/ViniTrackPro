package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.UserDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Role;
import com.matunda.vinitrackpro.model.User;
import com.matunda.vinitrackpro.repository.RoleRepository;
import com.matunda.vinitrackpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return convertToDTO(user);
    }

    public List<UserDTO> getActiveUsers() {
        return userRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return convertToDTO(user);
    }

    public List<UserDTO> getUsersByRole(String roleName) {
        Role.RoleType roleType = Role.RoleType.valueOf(roleName);
        return userRepository.findByRoles_Name(roleType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO updateUserProfile(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check if email is being changed and if it already exists
        if (!user.getEmail().equals(userDTO.getEmail()) &&
                userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Only update profile fields
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhone());
        //user.setLanguage(userDTO.getLanguage());

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public UserDTO toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setActive(!user.isActive());
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhone());
        //user.setLanguage(userDTO.getLanguage());
        user.setActive(userDTO.isActive());

        Set<Role> roles = new HashSet<>();
        userDTO.getRoles().forEach(roleName -> {
            Role role = roleRepository.findByName(Role.RoleType.valueOf(roleName))
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
            roles.add(role);
        });
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check if username is being changed and if it already exists
        if (!user.getUsername().equals(userDTO.getUsername()) && 
                userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email is being changed and if it already exists
        if (!user.getEmail().equals(userDTO.getEmail()) && 
                userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setUsername(userDTO.getUsername());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhone());
        //user.setLanguage(userDTO.getLanguage());
        user.setActive(userDTO.isActive());

        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            userDTO.getRoles().forEach(roleName -> {
                Role role = roleRepository.findByName(Role.RoleType.valueOf(roleName))
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
                roles.add(role);
            });
            user.setRoles(roles);
        }

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhoneNumber());
        //dto.setLanguage(user.getLanguage());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        dto.setRoles(roleNames);
        
        return dto;
    }
}
