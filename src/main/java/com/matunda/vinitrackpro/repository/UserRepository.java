//package com.matunda.vinitrackpro.repository;
//import com.matunda.vinitrackpro.model.User;
//import com.matunda.vinitrackpro.model.Role;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, Long>{
//
//    Optional<User> findByUsername(String username);
//    Optional<User> findByEmail(String email);
//
//    Optional<User> findAll(String fullName);
//    boolean existsByUsername(String username);
//    boolean existsByEmail(String email);
//    Optional<User> findByUsernameOrEmail(String username, String email);
//    List<User> findByActiveTrue();
//    List<User> findByRoles_Name(Role.RoleType roleName);
//}

package com.matunda.vinitrackpro.repository;

import com.matunda.vinitrackpro.model.User;
import com.matunda.vinitrackpro.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // Changed from findAll(String) to findByFullNameContaining
    List<User> findByFullNameContaining(String fullName);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    List<User> findByActiveTrue();
    List<User> findByRoles_Name(Role.RoleType roleName);
}