package com.maison.vinitrackpro.controller;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maison.vinitrackpro.dto.ProductionScheduleDTO;
import com.maison.vinitrackpro.model.ScheduleStatus;
import com.maison.vinitrackpro.service.ProductionScheduleService;

@RestController
@RequestMapping("/api/production-schedules")
// @CrossOrigin(origins = "*")
// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@CrossOrigin(
    origins = {"http://localhost:5173", "http://localhost:3000"},
    allowCredentials = "true"
)
public class ProductionScheduleController {

     private final ProductionScheduleService scheduleService;
    
    @Autowired
    public ProductionScheduleController(ProductionScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION')")
    public ResponseEntity<List<ProductionScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION')")
    public ResponseEntity<ProductionScheduleDTO> getScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION')")
    public ResponseEntity<ProductionScheduleDTO> createSchedule(
            @RequestBody ProductionScheduleDTO scheduleDTO,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scheduleService.createSchedule(scheduleDTO, userId));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION')")
    public ResponseEntity<ProductionScheduleDTO> updateSchedule(
            @PathVariable Long id,
            @RequestBody ProductionScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, scheduleDTO));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION')")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/status")
@PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION')")
    public ResponseEntity<ProductionScheduleDTO> changeScheduleStatus(
            @PathVariable Long id,
            @RequestParam ScheduleStatus status) {
        return ResponseEntity.ok(scheduleService.changeScheduleStatus(id, status));
    }
    
    @GetMapping("/line/{lineId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION')")
    public ResponseEntity<Object> getSchedulesByLine(
            @PathVariable Long lineId,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end) {
        if (start != null && end != null) {
            return ResponseEntity.ok(scheduleService.getSchedulesByLineAndDateRange(lineId, start, end));
        }
        return ResponseEntity.ok(scheduleService.getScheduleById(lineId));
    }
}
