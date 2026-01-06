package com.example.anpr.service;

import com.example.anpr.entity.ActualBusDay;
import com.example.anpr.entity.ActualEventEntity;
import com.example.anpr.entity.PlannedTripEntity;
import com.example.anpr.repository.ActualBusDayRepository;
import com.example.anpr.repository.ActualEventRepository;
import com.example.anpr.repository.PlannedTripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DataProcessingService {

    @Autowired
    private ActualEventRepository actualEventRepository;

    @Autowired
    private ActualBusDayRepository actualBusDayRepository;
    
    @Autowired
    private PlannedTripRepository plannedTripRepository;

    public void processAndSummarizeDay(String busNo, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        Optional<ActualEventEntity> firstOut = actualEventRepository
            .findFirstByBusNoAndDirectionAndEventTimeBetweenOrderByEventTimeAsc(busNo, "OUT", startOfDay, endOfDay);
            
        Optional<ActualEventEntity> lastIn = actualEventRepository
            .findFirstByBusNoAndDirectionAndEventTimeBetweenOrderByEventTimeDesc(busNo, "IN", startOfDay, endOfDay);

        // Use !isPresent() for Java 8-10 compatibility instead of isEmpty()
        if (!firstOut.isPresent() && !lastIn.isPresent()) {
            return;
        }

        ActualBusDay summary = actualBusDayRepository.findByBusNoAndDate(busNo, date)
            .orElse(new ActualBusDay());
        
        summary.setBusNo(busNo);
        summary.setDate(date);

        summary.setOutTime(firstOut.map(evt -> evt.getEventTime().toLocalTime()).orElse(null));
        summary.setInTime(lastIn.map(evt -> evt.getEventTime().toLocalTime()).orElse(null));
        
        List<PlannedTripEntity> trips = plannedTripRepository.findByBusNoOrderByTripNoAsc(busNo);
        if (!trips.isEmpty()) {
            summary.setRouteName(trips.get(0).getRouteName());
            summary.setRouteNo(trips.get(0).getRouteNo());
        }

        summary.setSourceCompleted(summary.getOutTime() != null && summary.getInTime() != null);
        
        actualBusDayRepository.save(summary);
    }
}
