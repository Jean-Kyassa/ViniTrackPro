package com.maison.vinitrackpro.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.dto.ProductionScheduleDTO;
import com.maison.vinitrackpro.dto.ProductionTaskDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.ProductionLine;
import com.maison.vinitrackpro.model.ProductionSchedule;
import com.maison.vinitrackpro.model.ProductionTask;
import com.maison.vinitrackpro.model.ScheduleStatus;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.repository.ProductRepository;
import com.maison.vinitrackpro.repository.ProductionLineRepository;
import com.maison.vinitrackpro.repository.ProductionScheduleRepository;
import com.maison.vinitrackpro.repository.UserRepository;

@Service
@Transactional
public class ProductionScheduleService {

    private final ProductionScheduleRepository scheduleRepository;
    private final ProductionLineRepository lineRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    
    @Autowired
    public ProductionScheduleService(ProductionScheduleRepository scheduleRepository,
                                   ProductionLineRepository lineRepository,
                                   UserRepository userRepository,
                                   ProductRepository productRepository,
                                   ModelMapper modelMapper) {
        this.scheduleRepository = scheduleRepository;
        this.lineRepository = lineRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }
    
    public ProductionScheduleDTO createSchedule(ProductionScheduleDTO scheduleDTO, Long userId) {
        ProductionSchedule schedule = new ProductionSchedule();
        mapDtoToEntity(scheduleDTO, schedule, userId);
        
        schedule.setStatus(ScheduleStatus.DRAFT);
        schedule.setCreatedAt(LocalDateTime.now());
        
        ProductionSchedule savedSchedule = scheduleRepository.save(schedule);
        return mapEntityToDto(savedSchedule);
    }
    
    public ProductionScheduleDTO updateSchedule(Long id, ProductionScheduleDTO scheduleDTO) {
        ProductionSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        
        mapDtoToEntity(scheduleDTO, schedule, schedule.getCreatedBy().getId());
        
        ProductionSchedule updatedSchedule = scheduleRepository.save(schedule);
        return mapEntityToDto(updatedSchedule);
    }
    
    public ProductionScheduleDTO getScheduleById(Long id) {
        ProductionSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        return mapEntityToDto(schedule);
    }
    
    public List<ProductionScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
    
    public void deleteSchedule(Long id) {
        ProductionSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        
        scheduleRepository.delete(schedule);
    }
    
    public ProductionScheduleDTO changeScheduleStatus(Long id, ScheduleStatus newStatus) {
        ProductionSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        
        schedule.setStatus(newStatus);
        ProductionSchedule updatedSchedule = scheduleRepository.save(schedule);
        return mapEntityToDto(updatedSchedule);
    }
    
    private void mapDtoToEntity(ProductionScheduleDTO dto, ProductionSchedule entity, Long userId) {
        modelMapper.map(dto, entity);
        
        if (dto.getProductionLineId() != null) {
            ProductionLine line = lineRepository.findById(dto.getProductionLineId())
                    .orElseThrow(() -> new ResourceNotFoundException("Production line not found"));
            entity.setLine(line);
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        entity.setCreatedBy(user);
        
        // Handle tasks if needed
    }
    
    private ProductionScheduleDTO mapEntityToDto(ProductionSchedule entity) {
        ProductionScheduleDTO dto = modelMapper.map(entity, ProductionScheduleDTO.class);
        
        if (entity.getLine() != null) {
            dto.setProductionLineId(entity.getLine().getId());
            dto.setProductionLineName(entity.getLine().getName());
        }
        
        if (entity.getCreatedBy() != null) {
            dto.setCreatedById(entity.getCreatedBy().getId());
            dto.setCreatedByUsername(entity.getCreatedBy().getUsername());
        }
        
        if (entity.getTasks() != null) {
            dto.setTasks(entity.getTasks().stream()
                    .map(this::mapTaskToDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    private ProductionTaskDTO mapTaskToDto(ProductionTask task) {
        ProductionTaskDTO dto = modelMapper.map(task, ProductionTaskDTO.class);
        
        if (task.getProduct() != null) {
            dto.setProductId(task.getProduct().getProductId());
            dto.setProductName(task.getProduct().getName());
        }
        
        return dto;
    }

    public Object getSchedulesByLineAndDateRange(Long lineId, LocalDateTime start, LocalDateTime end) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSchedulesByLineAndDateRange'");
    }
}
