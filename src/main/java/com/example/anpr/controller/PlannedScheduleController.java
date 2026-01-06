package com.example.anpr.controller;

import com.example.anpr.service.PlannedScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/planned")
public class PlannedScheduleController {

    @Autowired
    private PlannedScheduleService plannedScheduleService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadPlannedSchedule(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = plannedScheduleService.uploadPlannedTrips(file);
        return ResponseEntity.ok(result);
    }
}
