package com.matunda.vinitrackpro.repository;
import com.matunda.vinitrackpro.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByEmail(String email);
    List<Supplier> findByType(Supplier.SupplierType type);
    List<Supplier> findByActiveTrue();
    List<Supplier> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String nameQuery, String emailQuery);
}