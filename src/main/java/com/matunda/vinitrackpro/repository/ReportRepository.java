package com.matunda.vinitrackpro.repository;

import com.matunda.vinitrackpro.model.Report;
import com.matunda.vinitrackpro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByType(Report.ReportType type);
    List<Report> findByGeneratedBy(User user);
    List<Report> findByReportDateBetween(LocalDateTime start, LocalDateTime end);
    List<Report> findByPeriodStartGreaterThanEqualAndPeriodEndLessThanEqual(LocalDateTime start, LocalDateTime end);
}