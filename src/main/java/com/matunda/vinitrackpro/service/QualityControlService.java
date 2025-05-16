package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.QualityControlDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Product;
import com.matunda.vinitrackpro.model.QualityControl;
import com.matunda.vinitrackpro.model.User;
import com.matunda.vinitrackpro.repository.ProductRepository;
import com.matunda.vinitrackpro.repository.QualityControlRepository;
import com.matunda.vinitrackpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QualityControlService {

    private final QualityControlRepository qualityControlRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public List<QualityControlDTO> getAllQualityControls() {
        return qualityControlRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public QualityControlDTO getQualityControlById(Long id) {
        QualityControl qc = qualityControlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality control not found with id: " + id));
        return convertToDTO(qc);
    }

    @Transactional
    public QualityControlDTO updateQualityControlStatus(Long id, String status, String notes) {
        QualityControl qc = qualityControlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality control not found with id: " + id));

        QualityControl.QCStatus newStatus = QualityControl.QCStatus.valueOf(status);
        QualityControl.QCStatus oldStatus = qc.getStatus();

        // Validate status transition
        if (oldStatus == QualityControl.QCStatus.APPROVED || oldStatus == QualityControl.QCStatus.REJECTED) {
            throw new IllegalStateException("Cannot change status from " + oldStatus);
        }

        qc.setStatus(newStatus);

        // Update notes if provided
        if (notes != null && !notes.isEmpty()) {
            String updatedNotes = qc.getNotes() != null ? qc.getNotes() + "\n\n" : "";
            qc.setNotes(updatedNotes + "Status Update: " + newStatus + " - " + notes);
        }

        // Set approval date if transitioning to APPROVED/REJECTED
        if (newStatus == QualityControl.QCStatus.APPROVED || newStatus == QualityControl.QCStatus.REJECTED) {
            qc.setApprovalDate(LocalDateTime.now());
        }

        QualityControl updatedQC = qualityControlRepository.save(qc);

        // Create notification
        String message = String.format("Quality control status changed from %s to %s for batch %s",
                oldStatus, newStatus, qc.getBatchNumber());
        notificationService.createQualityControlNotification(updatedQC, message);

        return convertToDTO(updatedQC);
    }

    public QualityControlDTO getQualityControlByBatchNumber(String batchNumber) {
        QualityControl qc = qualityControlRepository.findByBatchNumber(batchNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Quality control not found with batch number: " + batchNumber));
        return convertToDTO(qc);
    }

    public List<QualityControlDTO> getQualityControlsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return qualityControlRepository.findByProduct(product).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<QualityControlDTO> getQualityControlsByStatus(String status) {
        QualityControl.QCStatus qcStatus = QualityControl.QCStatus.valueOf(status);
        return qualityControlRepository.findByStatus(qcStatus).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public QualityControlDTO createQualityControl(QualityControlDTO qcDTO) {
        Product product = productRepository.findById(qcDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + qcDTO.getProductId()));

        User inspector = userRepository.findById(qcDTO.getInspectorId())
                .orElseThrow(() -> new ResourceNotFoundException("Inspector not found with id: " + qcDTO.getInspectorId()));

        QualityControl qc = QualityControl.builder()
                .batchNumber(qcDTO.getBatchNumber() != null ? qcDTO.getBatchNumber() : generateBatchNumber())
                .product(product)
                .inspector(inspector)
                .inspectionDate(qcDTO.getInspectionDate() != null ? qcDTO.getInspectionDate() : LocalDateTime.now())
                .status(QualityControl.QCStatus.valueOf(qcDTO.getStatus()))
                .appearance(qcDTO.getAppearance())
                .aroma(qcDTO.getAroma())
                .taste(qcDTO.getTaste())
                .alcoholContent(qcDTO.getAlcoholContent())
                .acidity(qcDTO.getAcidity())
                .sugarContent(qcDTO.getSugarContent())
                .notes(qcDTO.getNotes())
                .build();

        QualityControl savedQC = qualityControlRepository.save(qc);

        // Create notification
        String message = String.format("New quality control created for product %s (Batch: %s)",
                product.getName(), savedQC.getBatchNumber());
        notificationService.createQualityControlNotification(savedQC, message);

        return convertToDTO(savedQC);
    }

    @Transactional
    public QualityControlDTO approveQualityControl(Long id, Long approverId, String notes) {
        QualityControl qc = qualityControlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality control not found with id: " + id));

        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("Approver not found with id: " + approverId));

        qc.setApprover(approver);
        qc.setApprovalDate(LocalDateTime.now());
        qc.setStatus(QualityControl.QCStatus.APPROVED);

        if (notes != null && !notes.isEmpty()) {
            qc.setNotes(qc.getNotes() + "\n\nApproval Notes: " + notes);
        }

        QualityControl updatedQC = qualityControlRepository.save(qc);

        // Create notification
        String message = String.format("Quality control approved for product %s (Batch: %s)",
                qc.getProduct().getName(), qc.getBatchNumber());
        notificationService.createQualityControlNotification(updatedQC, message);

        return convertToDTO(updatedQC);
    }

    @Transactional
    public QualityControlDTO rejectQualityControl(Long id, Long approverId, String reason) {
        QualityControl qc = qualityControlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality control not found with id: " + id));

        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("Approver not found with id: " + approverId));

        qc.setApprover(approver);
        qc.setApprovalDate(LocalDateTime.now());
        qc.setStatus(QualityControl.QCStatus.REJECTED);

        if (reason != null && !reason.isEmpty()) {
            qc.setNotes(qc.getNotes() + "\n\nRejection Reason: " + reason);
        }

        QualityControl updatedQC = qualityControlRepository.save(qc);

        // Create notification
        String message = String.format("Quality control rejected for product %s (Batch: %s)",
                qc.getProduct().getName(), qc.getBatchNumber());
        notificationService.createQualityControlNotification(updatedQC, message);

        return convertToDTO(updatedQC);
    }

    @Transactional
    public QualityControlDTO updateQualityControl(Long id, QualityControlDTO qcDTO) {
        QualityControl qc = qualityControlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quality control not found with id: " + id));

        Product product = productRepository.findById(qcDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + qcDTO.getProductId()));

        User inspector = userRepository.findById(qcDTO.getInspectorId())
                .orElseThrow(() -> new ResourceNotFoundException("Inspector not found with id: " + qcDTO.getInspectorId()));

        qc.setProduct(product);
        qc.setInspector(inspector);
        qc.setInspectionDate(qcDTO.getInspectionDate());
        qc.setStatus(QualityControl.QCStatus.valueOf(qcDTO.getStatus()));
        qc.setAppearance(qcDTO.getAppearance());
        qc.setAroma(qcDTO.getAroma());
        qc.setTaste(qcDTO.getTaste());
        qc.setAlcoholContent(qcDTO.getAlcoholContent());
        qc.setAcidity(qcDTO.getAcidity());
        qc.setSugarContent(qcDTO.getSugarContent());
        qc.setNotes(qcDTO.getNotes());

        QualityControl updatedQC = qualityControlRepository.save(qc);
        return convertToDTO(updatedQC);
    }

    @Transactional
    public void deleteQualityControl(Long id) {
        if (!qualityControlRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quality control not found with id: " + id);
        }
        qualityControlRepository.deleteById(id);
    }

    private String generateBatchNumber() {
        return "QC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private QualityControlDTO convertToDTO(QualityControl qc) {
        QualityControlDTO dto = new QualityControlDTO();
        dto.setId(qc.getId());
        dto.setBatchNumber(qc.getBatchNumber());
        dto.setProductId(qc.getProduct().getId());
        dto.setProductName(qc.getProduct().getName());
        dto.setInspectorId(qc.getInspector().getId());
        dto.setInspectedBy(qc.getInspector().getFullName());
        dto.setInspectionDate(qc.getInspectionDate());
        dto.setStatus(qc.getStatus().name());
        dto.setAppearance(qc.getAppearance());
        dto.setAroma(qc.getAroma());
        dto.setTaste(qc.getTaste());
        dto.setAlcoholContent(qc.getAlcoholContent());
        dto.setAcidity(qc.getAcidity());
        dto.setSugarContent(qc.getSugarContent());
        dto.setNotes(qc.getNotes());
        dto.setCreatedAt(qc.getCreatedAt());
        dto.setUpdatedAt(qc.getUpdatedAt());

        if (qc.getApprover() != null) {
            dto.setApprover(qc.getApprover());
            dto.setApprovedBy(qc.getApprover().getFullName());
            dto.setApprovalDate(qc.getApprovalDate());
        }

        return dto;
    }
}