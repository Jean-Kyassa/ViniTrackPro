package com.maison.vinitrackpro.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maison.vinitrackpro.dto.ReportDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.Report;
import com.maison.vinitrackpro.model.ReportType;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.repository.ReportRepository;
import com.maison.vinitrackpro.repository.UserRepository;

@Service
@Transactional
public class ReportService {

     private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReportService(ReportRepository reportRepository, 
                        UserRepository userRepository,
                        ModelMapper modelMapper) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public ReportDTO createReport(ReportDTO reportDTO, Long generatedById) {
        Report report = new Report();
        mapDtoToEntity(reportDTO, report, generatedById);
        report.setCreatedAt(LocalDateTime.now());
        
        Report savedReport = reportRepository.save(report);
        return mapEntityToDto(savedReport);
    }

    public ReportDTO updateReport(Long id, ReportDTO reportDTO) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        
        mapDtoToEntity(reportDTO, report, report.getGeneratedBy().getId());
        Report updatedReport = reportRepository.save(report);
        return mapEntityToDto(updatedReport);
    }

    public ReportDTO getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        return mapEntityToDto(report);
    }

    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public void deleteReport(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        reportRepository.delete(report);
    }

    public List<ReportDTO> getReportsByType(ReportType type) {
        return reportRepository.findByType(type).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<ReportDTO> getReportsByUser(Long userId) {
        return reportRepository.findByGeneratedBy_Id(userId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    private void mapDtoToEntity(ReportDTO dto, Report entity, Long generatedById) {
        modelMapper.map(dto, entity);
        
        if (generatedById != null) {
            User user = userRepository.findById(generatedById)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            entity.setGeneratedBy(user);
        }
    }

    private ReportDTO mapEntityToDto(Report entity) {
        ReportDTO dto = modelMapper.map(entity, ReportDTO.class);
        
        if (entity.getGeneratedBy() != null) {
            dto.setGeneratedById(entity.getGeneratedBy().getId());
            dto.setGeneratedByUsername(entity.getGeneratedBy().getUsername());
        }
        
        return dto;
    }
}
