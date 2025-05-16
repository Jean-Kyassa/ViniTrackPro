package com.matunda.vinitrackpro.service;

import com.matunda.vinitrackpro.dto.AuthResponseDTO;
import com.matunda.vinitrackpro.dto.LoginRequestDTO;
import com.matunda.vinitrackpro.dto.SignupRequestDTO;
import com.matunda.vinitrackpro.dto.UserDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Role;
import com.matunda.vinitrackpro.model.User;
import com.matunda.vinitrackpro.repository.RoleRepository;
import com.matunda.vinitrackpro.repository.UserRepository;
import com.matunda.vinitrackpro.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        // Determine if the login is using username or email
        String usernameOrEmail = loginRequest.getUsernameOrEmail();
        User user = usernameOrEmail.contains("@")
                ? userRepository.findByEmail(usernameOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + usernameOrEmail))
                : userRepository.findByUsername(usernameOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + usernameOrEmail));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), // Use the actual username for authentication
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        // Update last login time
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        UserDTO userDTO = convertToDTO(user);

        return new AuthResponseDTO(jwt, userDTO);
    }

    @Transactional
    public UserDTO registerUser(SignupRequestDTO signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        // Create new user's account
        User user = User.builder()
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .email(signupRequest.getEmail())
                .fullName(signupRequest.getFirstName() + " " + signupRequest.getLastName())
                .phoneNumber(null) // Can be updated later
                //.language("en") // Default language
                .active(true)
                .build();

        Set<Role> roles = new HashSet<>();

        // If no roles specified, assign default role (USER)
        if (signupRequest.getRoles() == null || signupRequest.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName(Role.RoleType.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
            roles.add(userRole);
        } else {
            signupRequest.getRoles().forEach(roleName -> {
                Role role = roleRepository.findByName(Role.RoleType.valueOf(roleName))
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName));
                roles.add(role);
            });
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    private UserDTO convertToDTO(User user) {
        String[] nameParts = user.getFullName().split(" ", 2);
        String fullName = nameParts.length > 0 ? nameParts[0] : "";
       // String lastName = nameParts.length > 1 ? nameParts[1] : "";

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(fullName);
        //dto.setLastName(lastName);
        dto.setFullName(user.getFullName());
        //dto.setLanguage(user.getLanguage());
        dto.setActive(user.isActive());
        dto.setRoles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));
        if (user.getLastLogin() != null) {
            dto.setLastLogin(user.getLastLogin());
        }
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }
}