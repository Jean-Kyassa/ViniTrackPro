package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.dto.UserDTO;
import com.matunda.vinitrackpro.service.AuditLogService;
import com.matunda.vinitrackpro.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/role/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String roleName) {
        return ResponseEntity.ok(userService.getUsersByRole(roleName));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        auditLogService.logActivity("CREATE", "User", createdUser.getId(),
                "Created new user: " + createdUser.getUsername());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        auditLogService.logActivity("UPDATE", "User", id,
                "Updated user: " + updatedUser.getUsername());
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/profile")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> updateUserProfile(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserProfile(id, userDTO);
        auditLogService.logActivity("UPDATE", "User", id,
                "Updated profile for user: " + updatedUser.getUsername());
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> toggleUserStatus(@PathVariable Long id) {
        UserDTO updatedUser = userService.toggleUserStatus(id);
        String status = updatedUser.isActive() ? "activated" : "deactivated";
        auditLogService.logActivity("UPDATE", "User", id,
                "User " + updatedUser.getUsername() + " " + status);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        auditLogService.logActivity("DELETE", "User", id, "Deleted user");
        return ResponseEntity.noContent().build();
    }
}
