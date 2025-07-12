package com.maison.vinitrackpro.service;

// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.stream.Collectors;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.maison.vinitrackpro.dto.QualityCheckDTO;
// import com.maison.vinitrackpro.dto.QualityCheckDTO.CreateQualityCheckDTO;
// import com.maison.vinitrackpro.model.ProductionBatch;
// import com.maison.vinitrackpro.model.QualityCheck;
// import com.maison.vinitrackpro.model.QualityCheckType;
// import com.maison.vinitrackpro.model.QualityStatus;
// import com.maison.vinitrackpro.model.User;
// import com.maison.vinitrackpro.repository.ProductionBatchRepository;
// import com.maison.vinitrackpro.repository.QualityCheckRepository;
// import com.maison.vinitrackpro.repository.UserRepository;

// import jakarta.persistence.EntityNotFoundException;

// @Service
// @Transactional
// public class QualityCheckService {

//      private final QualityCheckRepository qualityCheckRepository;
//     private final ProductionBatchRepository productionBatchRepository;
//     private final UserRepository userRepository;
    
//     public QualityCheckService(QualityCheckRepository qualityCheckRepository,
//                               ProductionBatchRepository productionBatchRepository,
//                               UserRepository userRepository) {
//         this.qualityCheckRepository = qualityCheckRepository;
//         this.productionBatchRepository = productionBatchRepository;
//         this.userRepository = userRepository;
//     }
    
//     @Transactional(readOnly = true)
//     public List<QualityCheckDTO> getAllQualityChecks() {
//         return qualityCheckRepository.findAll()
//                 .stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }
    
//     @Transactional(readOnly = true)
//     public Optional<QualityCheckDTO> getQualityCheckById(Long id) {
//         return qualityCheckRepository.findById(id)
//                 .map(this::convertToDTO);
//     }
    
//     @Transactional(readOnly = true)
//     public List<QualityCheckDTO> getQualityChecksByBatchId(Long batchId) {
//         return qualityCheckRepository.findByBatchId(batchId)
//                 .stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }
    
//     @Transactional(readOnly = true)
//     public List<QualityCheckDTO> getQualityChecksByStatus(QualityStatus status) {
//         return qualityCheckRepository.findByStatus(status)
//                 .stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }
    
//     @Transactional(readOnly = true)
//     public List<QualityCheckDTO> getQualityChecksByType(QualityCheckType type) {
//         return qualityCheckRepository.findByType(type)
//                 .stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }
    
//     @Transactional(readOnly = true)
//     public Page<QualityCheckDTO> getQualityChecksByStatus(QualityStatus status, Pageable pageable) {
//         return qualityCheckRepository.findByStatusOrderByCheckDateDesc(status, pageable)
//                 .map(this::convertToDTO);
//     }
    
//     @Transactional(readOnly = true)
//     public List<QualityCheckDTO> getQualityChecksByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
//         return qualityCheckRepository.findByCheckDateBetween(startDate, endDate)
//                 .stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }
    
//     public QualityCheckDTO createQualityCheck(CreateQualityCheckDTO createDTO) {
//         ProductionBatch batch = productionBatchRepository.findById(createDTO.getBatchId())
//                 .orElseThrow(() -> new EntityNotFoundException("Production batch not found with id: " + createDTO.getBatchId()));
        
//         User checkedBy = userRepository.findById(createDTO.getCheckedById())
//                 .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + createDTO.getCheckedById()));
        
//         QualityCheck qualityCheck = new QualityCheck();
//         qualityCheck.setName(createDTO.getName());
//         qualityCheck.setType(createDTO.getType());
//         qualityCheck.setBatch(batch);
//         qualityCheck.setCheckDate(createDTO.getCheckDate() != null ? createDTO.getCheckDate() : LocalDateTime.now());
//         qualityCheck.setStatus(createDTO.getStatus());
//         qualityCheck.setNotes(createDTO.getNotes());
//         qualityCheck.setCheckedBy(checkedBy);
//         qualityCheck.setCreatedAt(LocalDateTime.now());
        
//         QualityCheck savedQualityCheck = qualityCheckRepository.save(qualityCheck);
//         return convertToDTO(savedQualityCheck);
//     }
    
//     public QualityCheckDTO updateQualityCheck(Long id, CreateQualityCheckDTO updateDTO) {
//         QualityCheck existingQualityCheck = qualityCheckRepository.findById(id)
//                 .orElseThrow(() -> new EntityNotFoundException("Quality check not found with id: " + id));
        
//         if (updateDTO.getBatchId() != null && !updateDTO.getBatchId().equals(existingQualityCheck.getBatch().getId())) {
//             ProductionBatch batch = productionBatchRepository.findById(updateDTO.getBatchId())
//                     .orElseThrow(() -> new EntityNotFoundException("Production batch not found with id: " + updateDTO.getBatchId()));
//             existingQualityCheck.setBatch(batch);
//         }
        
//         if (updateDTO.getCheckedById() != null && !updateDTO.getCheckedById().equals(existingQualityCheck.getCheckedBy().getId())) {
//             User checkedBy = userRepository.findById(updateDTO.getCheckedById())
//                     .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + updateDTO.getCheckedById()));
//             existingQualityCheck.setCheckedBy(checkedBy);
//         }
        
//         existingQualityCheck.setName(updateDTO.getName());
//         existingQualityCheck.setType(updateDTO.getType());
//         existingQualityCheck.setCheckDate(updateDTO.getCheckDate());
//         existingQualityCheck.setStatus(updateDTO.getStatus());
//         existingQualityCheck.setNotes(updateDTO.getNotes());
        
//         QualityCheck updatedQualityCheck = qualityCheckRepository.save(existingQualityCheck);
//         return convertToDTO(updatedQualityCheck);
//     }
    
//     public void deleteQualityCheck(Long id) {
//         if (!qualityCheckRepository.existsById(id)) {
//             throw new EntityNotFoundException("Quality check not found with id: " + id);
//         }
//         qualityCheckRepository.deleteById(id);
//     }
//      @Transactional(readOnly = true)
//     public Map<QualityStatus, Long> getQualityCheckStatistics() {
//         Map<QualityStatus, Long> statistics = new HashMap<>();
//         for (QualityStatus status : QualityStatus.values()) {
//             statistics.put(status, qualityCheckRepository.countByStatus(status));
//         }
//         return statistics;
//     }
    
//     @Transactional(readOnly = true)
//     public List<QualityCheckDTO> searchQualityChecksByName(String name) {
//         return qualityCheckRepository.findByNameContainingIgnoreCase(name)
//                 .stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }
    
//     @Transactional(readOnly = true)
//     public List<QualityCheckDTO> getQualityChecksByBatchNumber(String batchNumber) {
//         return qualityCheckRepository.findByBatchNumber(batchNumber)
//                 .stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }
    
//     @Transactional(readOnly = true)
//     public List<QualityCheckDTO> getQualityChecksByBatchIdAndStatus(Long batchId, QualityStatus status) {
//         return qualityCheckRepository.findByBatchIdAndStatus(batchId, status)
//                 .stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }
    
//     @Transactional(readOnly = true)
//     public List<QualityCheckDTO> getQualityChecksByUser(Long userId) {
//         return qualityCheckRepository.findByCheckedById(userId)
//                 .stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }
    
//     public QualityCheckDTO updateQualityCheckStatus(Long id, QualityStatus newStatus) {
//         QualityCheck qualityCheck = qualityCheckRepository.findById(id)
//                 .orElseThrow(() -> new EntityNotFoundException("Quality check not found with id: " + id));
        
//         qualityCheck.setStatus(newStatus);
//         QualityCheck updatedQualityCheck = qualityCheckRepository.save(qualityCheck);
//         return convertToDTO(updatedQualityCheck);
//     }
    
//     private QualityCheckDTO convertToDTO(QualityCheck qualityCheck) {
//         return new QualityCheckDTO(
//                 qualityCheck.getId(),
//                 qualityCheck.getName(),
//                 qualityCheck.getType(),
//                 qualityCheck.getBatch().getId(),
//                 qualityCheck.getBatch().getBatchNumber(),
//                 qualityCheck.getCheckDate(),
//                 qualityCheck.getStatus(),
//                 qualityCheck.getNotes(),
//                 qualityCheck.getCheckedBy().getId(),
//                 qualityCheck.getCheckedBy().getUsername(), // Assuming User has getUsername() method
//                 qualityCheck.getCreatedAt()
//         );
//     }
// }


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.dto.CreateQualityCheckDTO;
import com.maison.vinitrackpro.dto.QualityCheckDTO;
import com.maison.vinitrackpro.model.ProductionBatch;
import com.maison.vinitrackpro.model.QualityCheck;
import com.maison.vinitrackpro.model.QualityCheckType;
import com.maison.vinitrackpro.model.QualityStatus;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.repository.ProductionBatchRepository;
import com.maison.vinitrackpro.repository.QualityCheckRepository;
import com.maison.vinitrackpro.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class QualityCheckService {
    
    private final QualityCheckRepository qualityCheckRepository;
    private final ProductionBatchRepository productionBatchRepository;
    private final UserRepository userRepository;
    
    public QualityCheckService(QualityCheckRepository qualityCheckRepository,
                              ProductionBatchRepository productionBatchRepository,
                              UserRepository userRepository) {
        this.qualityCheckRepository = qualityCheckRepository;
        this.productionBatchRepository = productionBatchRepository;
        this.userRepository = userRepository;
    }
    
    @Transactional(readOnly = true)
    public List<QualityCheckDTO> getAllQualityChecks() {
        return qualityCheckRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<QualityCheckDTO> getQualityCheckById(Long id) {
        return qualityCheckRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public List<QualityCheckDTO> getQualityChecksByBatchId(Long batchId) {
        return qualityCheckRepository.findByBatchId(batchId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<QualityCheckDTO> getQualityChecksByStatus(QualityStatus status) {
        return qualityCheckRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<QualityCheckDTO> getQualityChecksByType(QualityCheckType type) {
        return qualityCheckRepository.findByType(type)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<QualityCheckDTO> getQualityChecksByStatus(QualityStatus status, Pageable pageable) {
        return qualityCheckRepository.findByStatusOrderByCheckDateDesc(status, pageable)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public List<QualityCheckDTO> getQualityChecksByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return qualityCheckRepository.findByCheckDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // public QualityCheckDTO createQualityCheck(CreateQualityCheckDTO createDTO) {
    //     ProductionBatch batch = productionBatchRepository.findById(createDTO.getBatchId())
    //             .orElseThrow(() -> new EntityNotFoundException("Production batch not found with id: " + createDTO.getBatchId()));
        
    //     User checkedBy = userRepository.findById(createDTO.getCheckedById())
    //             .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + createDTO.getCheckedById()));
        
    //     QualityCheck qualityCheck = new QualityCheck();
    //     qualityCheck.setName(createDTO.getName());
    //     qualityCheck.setType(createDTO.getType());
    //     qualityCheck.setBatch(batch);
    //     qualityCheck.setCheckDate(createDTO.getCheckDate() != null ? createDTO.getCheckDate() : LocalDateTime.now());
    //     qualityCheck.setStatus(createDTO.getStatus());
    //     qualityCheck.setNotes(createDTO.getNotes());
    //     qualityCheck.setCheckedBy(checkedBy);
    //     qualityCheck.setCreatedAt(LocalDateTime.now());
        
    //     QualityCheck savedQualityCheck = qualityCheckRepository.save(qualityCheck);
    //     return convertToDTO(savedQualityCheck);
    // }

    public QualityCheckDTO createQualityCheck(CreateQualityCheckDTO createDTO) {
        System.out.println("Creating quality check with DTO: " + createDTO);
        System.out.println("Name: " + createDTO.getName());
        System.out.println("Type: " + createDTO.getType());
        System.out.println("BatchId: " + createDTO.getBatchId());
        System.out.println("CheckedById: " + createDTO.getCheckedById());
        
        ProductionBatch batch = productionBatchRepository.findById(createDTO.getBatchId())
                .orElseThrow(() -> new EntityNotFoundException("Production batch not found with id: " + createDTO.getBatchId()));
                
        User checkedBy = userRepository.findById(createDTO.getCheckedById())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + createDTO.getCheckedById()));
                
        QualityCheck qualityCheck = new QualityCheck();
        qualityCheck.setName(createDTO.getName());
        qualityCheck.setType(createDTO.getType());
        qualityCheck.setBatch(batch);
        qualityCheck.setCheckDate(createDTO.getCheckDate() != null ? createDTO.getCheckDate() : LocalDateTime.now());
        qualityCheck.setStatus(createDTO.getStatus());
        qualityCheck.setNotes(createDTO.getNotes());
        qualityCheck.setCheckedBy(checkedBy);
        qualityCheck.setCreatedAt(LocalDateTime.now());
                
        QualityCheck savedQualityCheck = qualityCheckRepository.save(qualityCheck);
        return convertToDTO(savedQualityCheck);
    }    
    
    public QualityCheckDTO updateQualityCheck(Long id, CreateQualityCheckDTO updateDTO) {
        QualityCheck existingQualityCheck = qualityCheckRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quality check not found with id: " + id));
        
        if (updateDTO.getBatchId() != null && !updateDTO.getBatchId().equals(existingQualityCheck.getBatch().getId())) {
            ProductionBatch batch = productionBatchRepository.findById(updateDTO.getBatchId())
                    .orElseThrow(() -> new EntityNotFoundException("Production batch not found with id: " + updateDTO.getBatchId()));
            existingQualityCheck.setBatch(batch);
        }
        
        if (updateDTO.getCheckedById() != null && !updateDTO.getCheckedById().equals(existingQualityCheck.getCheckedBy().getId())) {
            User checkedBy = userRepository.findById(updateDTO.getCheckedById())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + updateDTO.getCheckedById()));
            existingQualityCheck.setCheckedBy(checkedBy);
        }
        
        existingQualityCheck.setName(updateDTO.getName());
        existingQualityCheck.setType(updateDTO.getType());
        existingQualityCheck.setCheckDate(updateDTO.getCheckDate());
        existingQualityCheck.setStatus(updateDTO.getStatus());
        existingQualityCheck.setNotes(updateDTO.getNotes());
        
        QualityCheck updatedQualityCheck = qualityCheckRepository.save(existingQualityCheck);
        return convertToDTO(updatedQualityCheck);
    }
    
    public void deleteQualityCheck(Long id) {
        if (!qualityCheckRepository.existsById(id)) {
            throw new EntityNotFoundException("Quality check not found with id: " + id);
        }
        qualityCheckRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public Map<QualityStatus, Long> getQualityCheckStatistics() {
        Map<QualityStatus, Long> statistics = new HashMap<>();
        for (QualityStatus status : QualityStatus.values()) {
            statistics.put(status, qualityCheckRepository.countByStatus(status));
        }
        return statistics;
    }
    
    @Transactional(readOnly = true)
    public List<QualityCheckDTO> searchQualityChecksByName(String name) {
        return qualityCheckRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<QualityCheckDTO> getQualityChecksByBatchNumber(String batchNumber) {
        return qualityCheckRepository.findByBatchNumber(batchNumber)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<QualityCheckDTO> getQualityChecksByBatchIdAndStatus(Long batchId, QualityStatus status) {
        return qualityCheckRepository.findByBatchIdAndStatus(batchId, status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<QualityCheckDTO> getQualityChecksByUser(Long userId) {
        return qualityCheckRepository.findByCheckedById(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public QualityCheckDTO updateQualityCheckStatus(Long id, QualityStatus newStatus) {
        QualityCheck qualityCheck = qualityCheckRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quality check not found with id: " + id));
        
        qualityCheck.setStatus(newStatus);
        QualityCheck updatedQualityCheck = qualityCheckRepository.save(qualityCheck);
        return convertToDTO(updatedQualityCheck);
    }
    
    private QualityCheckDTO convertToDTO(QualityCheck qualityCheck) {
        return new QualityCheckDTO(
                qualityCheck.getId(),
                qualityCheck.getName(),
                qualityCheck.getType(),
                qualityCheck.getBatch().getId(),
                qualityCheck.getBatch().getBatchNumber(),
                qualityCheck.getCheckDate(),
                qualityCheck.getStatus(),
                qualityCheck.getNotes(),
                qualityCheck.getCheckedBy().getId(),
                qualityCheck.getCheckedBy().getUsername(), // Assuming User has getUsername() method
                qualityCheck.getCreatedAt()
        );
    }
}