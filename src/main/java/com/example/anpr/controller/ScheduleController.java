package com.example.anpr.controller;

import com.example.anpr.dto.BusDetailReportDTO;
import com.example.anpr.dto.DailyBusStatusDTO;
import com.example.anpr.dto.DelayLeaderboardDTO;
import com.example.anpr.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/compare")
    public List<DailyBusStatusDTO> compareSchedules(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return scheduleService.compareSchedule(date);
    }

    @GetMapping("/bus/{busNo}")
    public BusDetailReportDTO getBusDetailReport(
            @PathVariable String busNo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return scheduleService.getBusDetailReport(busNo, date);
    }

    @GetMapping("/delays/top")
    public List<DelayLeaderboardDTO> getDelayLeaderboard(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return scheduleService.getDelayLeaderboard(date);
    }

    @GetMapping("/missed")
    public List<DailyBusStatusDTO> getMissedBuses(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return scheduleService.getMissedBuses(date);
    }
}
