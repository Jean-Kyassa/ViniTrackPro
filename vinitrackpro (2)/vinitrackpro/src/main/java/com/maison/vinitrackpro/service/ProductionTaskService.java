package com.maison.vinitrackpro.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.maison.vinitrackpro.dto.CreateProductionTaskDTO;
import com.maison.vinitrackpro.dto.ProductionTaskDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.Product;
import com.maison.vinitrackpro.model.ProductionSchedule;
import com.maison.vinitrackpro.model.ProductionTask;
import com.maison.vinitrackpro.model.TaskStatus;
import com.maison.vinitrackpro.repository.ProductRepository;
import com.maison.vinitrackpro.repository.ProductionScheduleRepository;
import com.maison.vinitrackpro.repository.ProductionTaskRepository;

@Service
@Transactional
public class ProductionTaskService {

    @Autowired
    private ProductionTaskRepository productionTaskRepository;
    
    @Autowired
    private ProductionScheduleRepository productionScheduleRepository;
    
    @Autowired
    private ProductRepository productRepository;

    public ProductionTaskDTO createTask(CreateProductionTaskDTO createDTO) {
        // Validate schedule exists
        ProductionSchedule schedule = productionScheduleRepository.findById(createDTO.getScheduleId())
            .orElseThrow(() -> new ResourceNotFoundException("Production schedule not found with id: " + createDTO.getScheduleId()));
        
        // Validate product exists
        Product product = productRepository.findById(createDTO.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + createDTO.getProductId()));

        ProductionTask task = ProductionTask.builder()
            .name(createDTO.getName())
            .description(createDTO.getDescription())
            .schedule(schedule)
            .product(product)
            .quantity(createDTO.getQuantity())
            .priority(createDTO.getPriority())
            .status(createDTO.getStatus())
            .estimatedStart(createDTO.getEstimatedStart())
            .estimatedEnd(createDTO.getEstimatedEnd())
            .build();

        ProductionTask savedTask = productionTaskRepository.save(task);
        return convertToDTO(savedTask);
    }

    @Transactional(readOnly = true)
    public ProductionTaskDTO getTaskById(Long id) {
        ProductionTask task = productionTaskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Production task not found with id: " + id));
        return convertToDTO(task);
    }

    @Transactional(readOnly = true)
    public List<ProductionTaskDTO> getAllTasks() {
        return productionTaskRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductionTaskDTO> getTasksByStatus(TaskStatus status) {
        return productionTaskRepository.findByStatus(status).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductionTaskDTO> getTasksBySchedule(Long scheduleId) {
        return productionTaskRepository.findByScheduleId(scheduleId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public ProductionTaskDTO updateTask(Long id, CreateProductionTaskDTO updateDTO) {
        ProductionTask existingTask = productionTaskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Production task not found with id: " + id));

        // Validate schedule exists
        ProductionSchedule schedule = productionScheduleRepository.findById(updateDTO.getScheduleId())
            .orElseThrow(() -> new ResourceNotFoundException("Production schedule not found with id: " + updateDTO.getScheduleId()));
        
        // Validate product exists
        Product product = productRepository.findById(updateDTO.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + updateDTO.getProductId()));

        existingTask.setName(updateDTO.getName());
        existingTask.setDescription(updateDTO.getDescription());
        existingTask.setSchedule(schedule);
        existingTask.setProduct(product);
        existingTask.setQuantity(updateDTO.getQuantity());
        existingTask.setPriority(updateDTO.getPriority());
        existingTask.setStatus(updateDTO.getStatus());
        existingTask.setEstimatedStart(updateDTO.getEstimatedStart());
        existingTask.setEstimatedEnd(updateDTO.getEstimatedEnd());

        ProductionTask updatedTask = productionTaskRepository.save(existingTask);
        return convertToDTO(updatedTask);
    }
    public ProductionTaskDTO startTask(Long id) {
        ProductionTask task = productionTaskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Production task not found with id: " + id));
        
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setActualStart(LocalDateTime.now());
        
        ProductionTask updatedTask = productionTaskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    public ProductionTaskDTO completeTask(Long id) {
        ProductionTask task = productionTaskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Production task not found with id: " + id));
        
        task.setStatus(TaskStatus.COMPLETED);
        task.setActualEnd(LocalDateTime.now());
        
        ProductionTask updatedTask = productionTaskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    public void deleteTask(Long id) {
        if (!productionTaskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Production task not found with id: " + id);
        }
        productionTaskRepository.deleteById(id);
    }

    private ProductionTaskDTO convertToDTO(ProductionTask task) {
        return ProductionTaskDTO.builder()
            .id(task.getId())
            .name(task.getName())
            .description(task.getDescription())
            .productId(task.getProduct().getProductId())
            .productName(task.getProduct().getName())
            .quantity(task.getQuantity())
            .priority(task.getPriority())
            .status(task.getStatus())
            .estimatedStart(task.getEstimatedStart())
            .estimatedEnd(task.getEstimatedEnd())
            .build();
    }
}
