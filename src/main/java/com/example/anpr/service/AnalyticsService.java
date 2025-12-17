package com.example.anpr.service;

import com.example.anpr.dto.*;
import com.example.anpr.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private EventRepository eventRepository;

    private static final DateTimeFormatter HOURLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Map<String, Object> getOverview(LocalDateTime start, LocalDateTime end) {
        long totalEvents = eventRepository.countByEventTimeBetween(start, end);
        List<CountDTO> statusCounts = eventRepository.countByStatus(start, end);
        long uniqueVehicles = eventRepository.countDistinctPlateNumberByEventTimeBetween(start, end);

        Map<String, Object> overview = new HashMap<>();
        overview.put("totalEvents", totalEvents);
        overview.put("statusCounts", statusCounts);
        overview.put("uniqueVehicles", uniqueVehicles);
        return overview;
    }

    public List<TimeSeriesDTO> getHourlyTrend(LocalDateTime start, LocalDateTime end) {
        List<Object[]> results = eventRepository.getHourlyTrend(start, end);
        return results.stream()
                .map(row -> {
                    LocalDateTime time = LocalDateTime.parse((String) row[0], HOURLY_FORMATTER);
                    long count = ((Number) row[1]).longValue();
                    return new TimeSeriesDTO(time, count);
                })
                .collect(Collectors.toList());
    }

    public List<TimeSeriesDTO> getDailyTrend(LocalDateTime start, LocalDateTime end) {
        List<Object[]> results = eventRepository.getDailyTrend(start, end);
        return results.stream()
                .map(row -> {
                    LocalDateTime time = ((Date) row[0]).toLocalDate().atStartOfDay();
                    long count = ((Number) row[1]).longValue();
                    return new TimeSeriesDTO(time, count);
                })
                .collect(Collectors.toList());
    }

    public List<JunctionCountDTO> getTopJunctions(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findTopJunctionsByViolations(start, end);
    }

    public List<CountDTO> getTopCameras(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findTopCamerasByViolations(start, end);
    }

    public List<VehicleCountDTO> getRepeatOffenders(LocalDateTime start, LocalDateTime end, long min) {
        return eventRepository.findRepeatOffenders(start, end, min);
    }

    public List<CountDTO> getViolationsByColor(LocalDateTime start, LocalDateTime end) {
        return eventRepository.countByColor(start, end);
    }

    public List<CountDTO> getSpeedDistribution(LocalDateTime start, LocalDateTime end) {
        return eventRepository.getSpeedDistribution(start, end);
    }
}
