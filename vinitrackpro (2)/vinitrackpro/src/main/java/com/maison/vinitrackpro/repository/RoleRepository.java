package com.maison.vinitrackpro.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maison.vinitrackpro.model.ERole;
import com.maison.vinitrackpro.model.Role;

public interface RoleRepository  extends JpaRepository<Role, Integer> {
    boolean existsByName(ERole name);
    Optional<Role> findByName(ERole name);
}