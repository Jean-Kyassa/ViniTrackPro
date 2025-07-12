package com.maison.vinitrackpro.repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maison.vinitrackpro.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find product by exact name
    Optional<Product> findByName(String name);
    
    // Find product by code
    Optional<Product> findByCode(String code);
    
    // Find products by category
    List<Product> findByCategory(String category);
    
    // Search products by name containing (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Find products with price less than or equal to
    List<Product> findByPriceLessThanEqual(BigDecimal price);
    
    // Find products created after a certain date
    List<Product> findByCreatedAtAfter(LocalDateTime date);
    
    // Custom query for finding products by multiple categories
    @Query("SELECT p FROM Product p WHERE p.category IN :categories")
    List<Product> findByCategories(@Param("categories") List<String> categories);
    
    // Check if product with given code exists (for validation)
    boolean existsByCode(String code);
}