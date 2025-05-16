package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.ProductDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Product;
import com.matunda.vinitrackpro.repository.CategoryRepository;
import com.matunda.vinitrackpro.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getActiveProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByType(String type) {
        Product.ProductType productType = Product.ProductType.valueOf(type);
        return productRepository.findByType(productType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    public ProductDTO getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
        return convertToDTO(product);
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        // Verify category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDTO toggleProductStatus(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(!product.isActive());
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productRepository.findBySku(productDTO.getSku()).isPresent()) {
            throw new IllegalArgumentException("Product with SKU " + productDTO.getSku() + " already exists");
        }

        Product product = new Product();
        product.setSku(productDTO.getSku());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setType(Product.ProductType.valueOf(productDTO.getType().toString()));
        product.setPrice(productDTO.getPrice());
        product.setVintage(productDTO.getVintage());
        product.setVarietal(productDTO.getVarietal());
        product.setRegion(productDTO.getRegion());
        product.setImageUrl(productDTO.getImageUrl());
        product.setActive(productDTO.isActive());

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Check if SKU is being changed and if it already exists
        if (!product.getSku().equals(productDTO.getSku()) && 
                productRepository.findBySku(productDTO.getSku()).isPresent()) {
            throw new IllegalArgumentException("Product with SKU " + productDTO.getSku() + " already exists");
        }

        product.setSku(productDTO.getSku());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setType(Product.ProductType.valueOf(productDTO.getType().toString()));
        product.setPrice(productDTO.getPrice());
        product.setVintage(productDTO.getVintage());
        product.setVarietal(productDTO.getVarietal());
        product.setRegion(productDTO.getRegion());
        product.setImageUrl(productDTO.getImageUrl());
        product.setActive(productDTO.isActive());

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setSku(product.getSku());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setType(product.getType());
        dto.setPrice(product.getPrice());
        dto.setVintage(product.getVintage());
        dto.setVarietal(product.getVarietal());
        dto.setRegion(product.getRegion());
        dto.setImageUrl(product.getImageUrl());
        dto.setActive(product.isActive());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }}
