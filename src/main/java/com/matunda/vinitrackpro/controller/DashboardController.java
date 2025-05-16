package com.matunda.vinitrackpro.controller;
import com.matunda.vinitrackpro.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.matunda.vinitrackpro.dto.DashboardDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DashboardDTO> getDashboardData() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }
}
