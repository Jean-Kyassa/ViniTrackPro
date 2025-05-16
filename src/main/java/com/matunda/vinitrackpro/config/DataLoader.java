package com.matunda.vinitrackpro.config;
import com.matunda.vinitrackpro.model.*;
import com.matunda.vinitrackpro.repository.RoleRepository;
import com.matunda.vinitrackpro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.matunda.vinitrackpro.model.Role.RoleType;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create roles if they don't exist
        createRoleIfNotFound(RoleType.ROLE_COORDINATOR);
        createRoleIfNotFound(RoleType.ROLE_USER);
        createRoleIfNotFound(RoleType.ROLE_INDUSTRY_MANAGER);
        createRoleIfNotFound(RoleType.ROLE_SALES_STAFF);
        createRoleIfNotFound(RoleType.ROLE_LOGISTICS_EXPERT);
        createRoleIfNotFound(RoleType.ROLE_LOGISTICS_MANAGER);
        createRoleIfNotFound(RoleType.ROLE_DISTRIBUTION_MANAGER);
        createRoleIfNotFound(RoleType.ROLE_ACCOUNTANT);
        createRoleIfNotFound(RoleType.ROLE_QUALITY_CONTROL);
        createRoleIfNotFound(RoleType.ROLE_CLIENT);

        // Create admin user if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setFullName("Admin");
            admin.setUsername("admin");
            admin.setEmail("admin@vinitrackpro.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now()); // Set this explicitly
            admin.setUpdatedAt(LocalDateTime.now());
            
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(RoleType.ROLE_COORDINATOR).orElseThrow(() -> 
                    new RuntimeException("Admin role not found")));
            admin.setRoles(roles);
            
            userRepository.save(admin);
        }
    }

    private void createRoleIfNotFound(RoleType name) {
        if (!roleRepository.existsByName(name)) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }
}
