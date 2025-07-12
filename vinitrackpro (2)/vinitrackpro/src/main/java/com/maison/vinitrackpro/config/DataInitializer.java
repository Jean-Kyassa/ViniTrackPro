package com.maison.vinitrackpro.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.maison.vinitrackpro.model.ERole;
import com.maison.vinitrackpro.model.Role;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.repository.RoleRepository;
import com.maison.vinitrackpro.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize all system roles
        initRoles();
        
        // Create default admin user
        createAdminUser();
        
        // Create other default users (optional)
        createDefaultUsers();
    }

    private void initRoles() {
        // Check if roles already exist to avoid duplicates
        for (ERole role : ERole.values()) {
            if (!roleRepository.existsByName(role)) {
                Role newRole = new Role();
                newRole.setName(role);
                roleRepository.save(newRole);
            }
        }
    }

    private void createAdminUser() {
        // Create admin user if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@vinitrackpro.com");
            admin.setPassword(passwordEncoder.encode("admin123"));

            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Admin Role not found."));
            roles.add(adminRole);
            
            // Optionally add other roles to admin
            roleRepository.findByName(ERole.ROLE_INVENTORY).ifPresent(roles::add);
            roleRepository.findByName(ERole.ROLE_QUALITY).ifPresent(roles::add);
            
            admin.setRoles(roles);

            userRepository.save(admin);
            System.out.println("Created admin user with username: admin");
        }
    }

    private void createDefaultUsers() {
        // Create inventory manager user
        createUserIfNotExists(
            "inventory",
            "inventory@vinitrackpro.com",
            "inventory123",
            ERole.ROLE_INVENTORY
        );

        // Create logistics user
        createUserIfNotExists(
            "logistics",
            "logistics@vinitrackpro.com",
            "logistics123",
            ERole.ROLE_LOGISTICS
        );

        // Create quality control user
        createUserIfNotExists(
            "quality",
            "quality@vinitrackpro.com",
            "quality123",
            ERole.ROLE_QUALITY
        );

        // Create regular user
        createUserIfNotExists(
            "user",
            "user@vinitrackpro.com",
            "user123",
            ERole.ROLE_USER
        );
    }

    private void createUserIfNotExists(String username, String email, String password, ERole role) {
        if (!userRepository.existsByUsername(username)) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));

            Set<Role> roles = new HashSet<>();
            roleRepository.findByName(role)
                    .ifPresentOrElse(
                        roles::add,
                        () -> { throw new RuntimeException("Error: " + role + " Role not found."); }
                    );
            
            user.setRoles(roles);

            userRepository.save(user);
            System.out.println("Created user with username: " + username);
        }
    }
}