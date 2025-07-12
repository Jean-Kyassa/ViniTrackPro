package com.maison.vinitrackpro.controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maison.vinitrackpro.service.DeliveryRouteService;
import com.maison.vinitrackpro.service.DriverService;
import com.maison.vinitrackpro.service.ProductionBatchService;
import com.maison.vinitrackpro.service.ProductionLineService;
import com.maison.vinitrackpro.service.ProductionTaskService;
import com.maison.vinitrackpro.service.VehicleService;

@RestController
@RequestMapping("/api/metrics")
// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@CrossOrigin(
    origins = {"http://localhost:5173", "http://localhost:3000"},
    allowCredentials = "true"
)
public class ProductionMetricsController {

    @Autowired
    private ProductionBatchService batchService;
    
    @Autowired
    private ProductionLineService lineService;
    
    @Autowired
    private ProductionTaskService taskService;
    
    @Autowired
    private DriverService driverService;
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private DeliveryRouteService routeService;

    @GetMapping("/production")
    public ResponseEntity<Map<String, Object>> getProductionMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            var batches = batchService.getAllBatches();
            var lines = lineService.getAllProductionLines();
            var tasks = taskService.getAllTasks();
            
            // Calculate metrics
            long activeBatches = batches.stream()
                .filter(batch -> batch.getExpiryDate().isAfter(java.time.LocalDate.now()))
                .count();
            
            long operationalLines = lines.stream()
                .filter(line -> "OPERATIONAL".equals(line.getStatus().toString()))
                .count();
            
            long completedTasks = tasks.stream()
                .filter(task -> "COMPLETED".equals(task.getStatus().toString()))
                .count();
            
            long pendingTasks = tasks.stream()
                .filter(task -> "PENDING".equals(task.getStatus().toString()))
                .count();
            
            double averageEfficiency = lines.stream()
                .mapToDouble(line -> line.getEfficiency())
                .average()
                .orElse(0.0);
            
            metrics.put("totalProducts", 0); // You'll need to implement product count
            metrics.put("totalBatches", batches.size());
            metrics.put("activeBatches", activeBatches);
            metrics.put("totalProductionLines", lines.size());
            metrics.put("operationalLines", operationalLines);
            metrics.put("totalTasks", tasks.size());
            metrics.put("completedTasks", completedTasks);
            metrics.put("pendingTasks", pendingTasks);
            metrics.put("averageEfficiency", Math.round(averageEfficiency * 100.0) / 100.0);
            
        } catch (Exception e) {
            // Return default values if services fail
            metrics.put("totalProducts", 0);
            metrics.put("totalBatches", 0);
            metrics.put("activeBatches", 0);
            metrics.put("totalProductionLines", 0);
            metrics.put("operationalLines", 0);
            metrics.put("totalTasks", 0);
            metrics.put("completedTasks", 0);
            metrics.put("pendingTasks", 0);
            metrics.put("averageEfficiency", 0.0);
        }
        
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/delivery")
    public ResponseEntity<Map<String, Object>> getDeliveryMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            var drivers = driverService.getAllDrivers();
            var vehicles = vehicleService.getAllVehicles();
            var routes = routeService.getAllRoutes();
            
            long activeDrivers = drivers.stream()
                .filter(driver -> "ACTIVE".equals(driver.getStatus().toString()))
                .count();
            
            metrics.put("totalOrders", 0); // You'll need to implement order count
            metrics.put("pendingOrders", 0);
            metrics.put("processingOrders", 0);
            metrics.put("inTransitOrders", 0);
            metrics.put("deliveredOrders", 0);
            metrics.put("cancelledOrders", 0);
            metrics.put("totalDrivers", drivers.size());
            metrics.put("activeDrivers", activeDrivers);
            metrics.put("totalVehicles", vehicles.size());
            metrics.put("totalRoutes", routes.size());
            
        } catch (Exception e) {
            // Return default values if services fail
            metrics.put("totalOrders", 0);
            metrics.put("pendingOrders", 0);
            metrics.put("processingOrders", 0);
            metrics.put("inTransitOrders", 0);
            metrics.put("deliveredOrders", 0);
            metrics.put("cancelledOrders", 0);
            metrics.put("totalDrivers", 0);
            metrics.put("activeDrivers", 0);
            metrics.put("totalVehicles", 0);
            metrics.put("totalRoutes", 0);
        }
        
        return ResponseEntity.ok(metrics);
    }
}
