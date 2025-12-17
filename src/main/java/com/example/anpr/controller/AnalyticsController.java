package com.example.anpr.controller;

import com.example.anpr.dto.*;
import com.example.anpr.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/overview")
    public Map<String, Object> getOverview(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return analyticsService.getOverview(start, end);
    }

    @GetMapping("/trend/hourly")
    public List<TimeSeriesDTO> getHourlyTrend(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return analyticsService.getHourlyTrend(start, end);
    }

    @GetMapping("/trend/daily")
    public List<TimeSeriesDTO> getDailyTrend(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return analyticsService.getDailyTrend(start, end);
    }

    @GetMapping("/junctions/top")
    public List<JunctionCountDTO> getTopJunctions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return analyticsService.getTopJunctions(start, end);
    }

    @GetMapping("/cameras/top")
    public List<CountDTO> getTopCameras(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return analyticsService.getTopCameras(start, end);
    }

    @GetMapping("/vehicles/repeat")
    public List<VehicleCountDTO> getRepeatOffenders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "3") long min) {
        return analyticsService.getRepeatOffenders(start, end, min);
    }

    @GetMapping("/vehicles/colors")
    public List<CountDTO> getViolationsByColor(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return analyticsService.getViolationsByColor(start, end);
    }

    @GetMapping("/vehicles/speed")
    public List<CountDTO> getSpeedDistribution(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return analyticsService.getSpeedDistribution(start, end);
    }
}
