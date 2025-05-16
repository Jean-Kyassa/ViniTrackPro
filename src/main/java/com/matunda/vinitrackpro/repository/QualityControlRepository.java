package com.matunda.vinitrackpro.repository;
import com.matunda.vinitrackpro.model.Product;
import com.matunda.vinitrackpro.model.QualityControl;
import com.matunda.vinitrackpro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QualityControlRepository extends JpaRepository<QualityControl, Long> {
    Optional<QualityControl> findByBatchNumber(String batchNumber);
    List<QualityControl> findByProduct(Product product);
    List<QualityControl> findByInspector(User inspector);
    List<QualityControl> findByApprover(User approver);
    List<QualityControl> findByStatus(QualityControl.QCStatus status);
    List<QualityControl> findByInspectionDateBetween(LocalDateTime start, LocalDateTime end);
}
