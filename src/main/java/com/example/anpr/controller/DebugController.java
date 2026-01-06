package com.example.anpr.controller;

import com.example.anpr.entity.ActualBusDay;
import com.example.anpr.entity.ActualEventEntity;
import com.example.anpr.entity.PlannedTripEntity;
import com.example.anpr.repository.ActualBusDayRepository;
import com.example.anpr.repository.ActualEventRepository;
import com.example.anpr.repository.BusMasterRepository;
import com.example.anpr.repository.PlannedTripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private BusMasterRepository busMasterRepository;

    @Autowired
    private PlannedTripRepository plannedTripRepository;

    @Autowired
    private ActualEventRepository actualEventRepository;

    @Autowired
    private ActualBusDayRepository actualBusDayRepository;

    @GetMapping("/check-bus")
    public Map<String, Object> checkBus(
            @RequestParam String busNo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        Map<String, Object> result = new HashMap<>();
        // Explicitly using String type instead of var to ensure compatibility
        String normalizedBusNo = busNo.toUpperCase().replaceAll("[^A-Z0-9]", "");
        
        result.put("busNo_provided", busNo);
        result.put("busNo_normalized", normalizedBusNo);
        
        // 1. Check Master
        boolean inMaster = busMasterRepository.existsByBusNo(normalizedBusNo);
        result.put("inBusMaster", inMaster);
        
        // 2. Check Planned Trips
        List<PlannedTripEntity> trips = plannedTripRepository.findByBusNoOrderByTripNoAsc(normalizedBusNo);
        result.put("plannedTripsCount", trips.size());
        result.put("plannedTrips", trips);
        
        if (date != null) {
            // 3. Check Actual Events (Raw)
            List<ActualEventEntity> rawEvents = actualEventRepository.findByBusNoAndEventTimeBetweenOrderByEventTimeAsc(
                normalizedBusNo, date.atStartOfDay(), date.atTime(LocalTime.MAX));
            result.put("actualEventsCount_for_date", rawEvents.size());
            result.put("actualEvents_for_date", rawEvents);

            // 4. Check Actual Bus Day (Summary)
            Optional<ActualBusDay> summary = actualBusDayRepository.findByBusNoAndDate(normalizedBusNo, date);
            result.put("inActualBusDay_for_date", summary.isPresent());
            if (summary.isPresent()) {
                result.put("actualBusDay_summary", summary.get());
            }
        } else {
            result.put("date_info", "Provide a 'date' param (YYYY-MM-DD) to check actuals.");
        }
        
        return result;
    }
}
