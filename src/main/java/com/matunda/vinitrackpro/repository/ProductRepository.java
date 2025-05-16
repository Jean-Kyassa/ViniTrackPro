package com.matunda.vinitrackpro.repository;
import com.matunda.vinitrackpro.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findByType(Product.ProductType type);
    List<Product> findByActiveTrue();

    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameQuery, String descriptionQuery);
}