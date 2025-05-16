package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.CategoryDTO;
import com.matunda.vinitrackpro.exception.BadRequestException;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Category;
import com.matunda.vinitrackpro.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
    }
    
    public List<CategoryDTO> getActiveCategories() {
        return categoryRepository.findByActive(true).stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
    }
    
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return modelMapper.map(category, CategoryDTO.class);
    }
    
    public CategoryDTO getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", name));
        return modelMapper.map(category, CategoryDTO.class);
    }
    
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new BadRequestException("Category with name " + categoryDTO.getName() + " already exists");
        }
        
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setActive(true);
        Category savedCategory = categoryRepository.save(category);
        
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
    
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        // Check if name is being changed and if the new name already exists
        if (!category.getName().equals(categoryDTO.getName()) && 
                categoryRepository.existsByName(categoryDTO.getName())) {
            throw new BadRequestException("Category with name " + categoryDTO.getName() + " already exists");
        }
        
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }
    
    @Transactional
    public CategoryDTO toggleCategoryStatus(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        category.setActive(!category.isActive());
        Category updatedCategory = categoryRepository.save(category);
        
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }
    
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        // Check if category has products
        if (!category.getProducts().isEmpty()) {
            throw new BadRequestException("Cannot delete category with associated products");
        }
        
        categoryRepository.delete(category);
    }
}
