package com.maison.vinitrackpro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.dto.CreateProductDTO;
import com.maison.vinitrackpro.dto.ProductDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.Product;
import com.maison.vinitrackpro.repository.ProductRepository;

import jakarta.validation.ValidationException;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductDTO createProduct(CreateProductDTO createDTO) {
        // Validate product code uniqueness
        if (productRepository.existsByCode(createDTO.getCode())) {
            throw new ValidationException("Product code already exists: " + createDTO.getCode());
        }

        Product product = Product.builder()
            .name(createDTO.getName())
            .code(createDTO.getCode())
            .description(createDTO.getDescription())
            .category(createDTO.getCategory())
            .price(createDTO.getPrice())
            .unit(createDTO.getUnit())
            .build();

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public ProductDTO updateProduct(Long id, CreateProductDTO updateDTO) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Validate product code uniqueness (excluding current product)
        if (!existingProduct.getCode().equals(updateDTO.getCode()) &&
            productRepository.existsByCode(updateDTO.getCode())) {
            throw new ValidationException("Product code already exists: " + updateDTO.getCode());
        }

        existingProduct.setName(updateDTO.getName());
        existingProduct.setCode(updateDTO.getCode());
        existingProduct.setDescription(updateDTO.getDescription());
        existingProduct.setCategory(updateDTO.getCategory());
        existingProduct.setPrice(updateDTO.getPrice());
        existingProduct.setUnit(updateDTO.getUnit());

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
            .productId(product.getProductId())
            .name(product.getName())
            .code(product.getCode())
            .description(product.getDescription())
            .category(product.getCategory())
            .price(product.getPrice())
            .unit(product.getUnit())
            .createdAt(product.getCreatedAt())
            .build();
    }
}
