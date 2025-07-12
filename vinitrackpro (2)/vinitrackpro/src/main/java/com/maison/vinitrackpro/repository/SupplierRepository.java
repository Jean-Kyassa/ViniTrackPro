package com.maison.vinitrackpro.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.ProductCategory;
import com.maison.vinitrackpro.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Page<Supplier> findByCompanyNameContaining(String companyName, Pageable pageable);
    Page<Supplier> findByProductCategories(ProductCategory category, Pageable pageable);
}
