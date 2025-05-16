package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.ReportDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Report;
import com.matunda.vinitrackpro.model.User;
import com.matunda.vinitrackpro.repository.ReportRepository;
import com.matunda.vinitrackpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReportDTO getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        return convertToDTO(report);
    }

    public List<ReportDTO> getReportsByType(String type) {
        Report.ReportType reportType = Report.ReportType.valueOf(type);
        return reportRepository.findByType(reportType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReportDTO> getReportsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return reportRepository.findByGeneratedBy(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReportDTO> getReportsByDateRange(LocalDateTime start, LocalDateTime end) {
        return reportRepository.findByReportDateBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReportDTO> getReportsByPeriod(LocalDateTime start, LocalDateTime end) {
        return reportRepository.findByPeriodStartGreaterThanEqualAndPeriodEndLessThanEqual(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReportDTO createReport(ReportDTO reportDTO) {
        User generatedBy = userRepository.findById(reportDTO.getGeneratedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + reportDTO.getGeneratedBy()));

        Report report = Report.builder()
                .title(reportDTO.getTitle())
                .type(Report.ReportType.valueOf(reportDTO.getReportType()))
                .reportDate(reportDTO.getGeneratedAt() != null ? reportDTO.getGeneratedAt() : LocalDateTime.now())
                .periodStart(reportDTO.getCreatedAt()) // Using createdAt as periodStart
                .periodEnd(reportDTO.getUpdatedAt())   // Using updatedAt as periodEnd
                .content(reportDTO.getDescription())
                .fileUrl(reportDTO.getFileUrl())
                .generatedBy(generatedBy)
                .build();

        Report savedReport = reportRepository.save(report);
        return convertToDTO(savedReport);
    }

    @Transactional
    public ReportDTO updateReport(Long id, ReportDTO reportDTO) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        User generatedBy = userRepository.findById(reportDTO.getGeneratedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + reportDTO.getGeneratedBy()));

        report.setTitle(reportDTO.getTitle());
        report.setType(Report.ReportType.valueOf(reportDTO.getReportType()));
        report.setContent(reportDTO.getDescription());
        report.setFileUrl(reportDTO.getFileUrl());
        report.setGeneratedBy(generatedBy);

        // Update period dates if provided
        if (reportDTO.getCreatedAt() != null) {
            report.setPeriodStart(reportDTO.getCreatedAt());
        }
        if (reportDTO.getUpdatedAt() != null) {
            report.setPeriodEnd(reportDTO.getUpdatedAt());
        }

        Report updatedReport = reportRepository.save(report);
        return convertToDTO(updatedReport);
    }

    @Transactional
    public void deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Report not found with id: " + id);
        }
        reportRepository.deleteById(id);
    }

    private ReportDTO convertToDTO(Report report) {
        ReportDTO dto = new ReportDTO();
        dto.setTitle(report.getTitle());
        dto.setDescription(report.getContent());
        dto.setReportType(report.getType().name());
        dto.setFileUrl(report.getFileUrl());
        dto.setGeneratedBy(report.getGeneratedBy().getId());
        dto.setGeneratedByName(report.getGeneratedBy().getFullName());
        dto.setGeneratedAt(report.getReportDate());
        dto.setCreatedAt(report.getPeriodStart()); // Mapping periodStart to createdAt
        dto.setUpdatedAt(report.getPeriodEnd());   // Mapping periodEnd to updatedAt
        return dto;
    }
}