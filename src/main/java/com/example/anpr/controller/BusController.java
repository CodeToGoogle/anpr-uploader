package com.example.anpr.controller;

import com.example.anpr.dto.*;
import com.example.anpr.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BusController {

    @Autowired
    private BusService busService;

    @PostMapping("/bus/upload-master")
    public ResponseEntity<?> uploadBusMaster(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = busService.uploadBusMaster(file);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/outshedding/upload")
    public ResponseEntity<?> uploadOutshedding(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = busService.uploadOutshedding(file);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/outshedding/availability")
    public DailyAvailabilityDTO getDailyAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return busService.getDailyAvailability(date);
    }

    @GetMapping("/outshedding/missing")
    public List<String> getMissingBuses(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return busService.getMissingBuses(date);
    }

    @GetMapping("/outshedding/shift-summary")
    public ShiftSummaryDTO getShiftSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return busService.getShiftSummary(date);
    }

    @GetMapping("/outshedding/bus/{busNo}")
    public List<BusHistoryDTO> getBusHistory(@PathVariable String busNo) {
        return busService.getBusHistory(busNo);
    }

    @GetMapping("/outshedding/missing/frequent")
    public List<AbsenteeDTO> getFrequentAbsentees(
            @RequestParam(defaultValue = "7") int days) {
        return busService.getFrequentAbsentees(days);
    }
}
