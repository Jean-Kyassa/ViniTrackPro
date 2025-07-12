package com.maison.vinitrackpro.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maison.vinitrackpro.dto.CreateProductionTaskDTO;
import com.maison.vinitrackpro.dto.ProductionTaskDTO;
import com.maison.vinitrackpro.model.TaskStatus;
import com.maison.vinitrackpro.service.ProductionTaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/production-tasks")
// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@CrossOrigin(
    origins = {"http://localhost:5173", "http://localhost:3000"},
    allowCredentials = "true"
)
public class ProductionTaskController {

    @Autowired
    private ProductionTaskService productionTaskService;

    @PostMapping
    public ResponseEntity<ProductionTaskDTO> createTask(@Valid @RequestBody CreateProductionTaskDTO createDTO) {
        ProductionTaskDTO createdTask = productionTaskService.createTask(createDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionTaskDTO> getTaskById(@PathVariable Long id) {
        ProductionTaskDTO task = productionTaskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<ProductionTaskDTO>> getAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long scheduleId) {
        List<ProductionTaskDTO> tasks;
        
        if (scheduleId != null) {
            tasks = productionTaskService.getTasksBySchedule(scheduleId);
        } else if (status != null) {
            tasks = productionTaskService.getTasksByStatus(status);
        } else {
            tasks = productionTaskService.getAllTasks();
        }
        
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionTaskDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductionTaskDTO updateDTO) {
        ProductionTaskDTO updatedTask = productionTaskService.updateTask(id, updateDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<ProductionTaskDTO> startTask(@PathVariable Long id) {
        ProductionTaskDTO startedTask = productionTaskService.startTask(id);
        return ResponseEntity.ok(startedTask);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ProductionTaskDTO> completeTask(@PathVariable Long id) {
        ProductionTaskDTO completedTask = productionTaskService.completeTask(id);
        return ResponseEntity.ok(completedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        productionTaskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
