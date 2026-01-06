package com.example.anpr.service;

import com.example.anpr.dto.*;
import com.example.anpr.entity.ActualBusDay;
import com.example.anpr.entity.ActualEventEntity;
import com.example.anpr.entity.PlannedTripEntity;
import com.example.anpr.repository.ActualBusDayRepository;
import com.example.anpr.repository.ActualEventRepository;
import com.example.anpr.repository.PlannedTripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private PlannedTripRepository plannedTripRepository;

    @Autowired
    private ActualBusDayRepository actualBusDayRepository;

    @Autowired
    private ActualEventRepository actualEventRepository;

    public List<DailyBusStatusDTO> compareSchedule(LocalDate date) {
        List<String> scheduledBuses = plannedTripRepository.findAllScheduledBusNos();
        
        return scheduledBuses.stream().map(busNo -> {
            DailyBusStatusDTO busStatus = new DailyBusStatusDTO();
            busStatus.setBusNo(busNo);
            
            // Fetch the pre-summarized data for the day
            Optional<ActualBusDay> summaryOpt = actualBusDayRepository.findByBusNoAndDate(busNo, date);
            
            List<PlannedTripEntity> plannedTrips = plannedTripRepository.findByBusNoOrderByTripNoAsc(busNo);
            List<TripComparisonDTO> tripComparisons = new ArrayList<>();
            
            for (PlannedTripEntity trip : plannedTrips) {
                TripComparisonDTO dto = new TripComparisonDTO();
                dto.setTripNo(trip.getTripNo());
                dto.setRouteName(trip.getRouteName());
                dto.setRouteNo(trip.getRouteNo());
                dto.setPlannedOut(trip.getPlannedOut());
                dto.setPlannedIn(trip.getPlannedIn());
                
                // Use the times from the summary table.
                // This assumes the summary's out_time is for Trip 1 and in_time is for the last trip.
                // This is a limitation of the summary table, but it's the required architecture.
                if (trip.getTripNo() == 1) {
                    summaryOpt.ifPresent(sum -> dto.setActualOut(sum.getOutTime()));
                }
                // For simplicity, we assign the single daily IN time to the last trip.
                if (trip.getTripNo() == plannedTrips.size()) {
                     summaryOpt.ifPresent(sum -> dto.setActualIn(sum.getInTime()));
                }
                
                calculateTripStatus(dto);
                tripComparisons.add(dto);
            }
            
            busStatus.setTrips(tripComparisons);
            calculateOverallStatus(busStatus);
            return busStatus;
        }).collect(Collectors.toList());
    }
    
    public BusDetailReportDTO getBusDetailReport(String busNo, LocalDate date) {
        BusDetailReportDTO report = new BusDetailReportDTO();
        List<DailyBusStatusDTO> comparisonList = compareSchedule(date);
        
        Optional<DailyBusStatusDTO> summaryOpt = comparisonList.stream()
            .filter(c -> c.getBusNo().equals(busNo))
            .findFirst();

        if (summaryOpt.isPresent()) {
            report.setSummary(summaryOpt.get());
            report.setActualEvents(actualEventRepository.findByBusNoAndEventTimeBetweenOrderByEventTimeAsc(busNo, date.atStartOfDay(), date.atTime(LocalTime.MAX)));
        }
        
        return report;
    }

    public List<DelayLeaderboardDTO> getDelayLeaderboard(LocalDate date) {
        List<DailyBusStatusDTO> comparisons = compareSchedule(date);
        return comparisons.stream()
            .map(bus -> {
                long totalDelay = bus.getTrips().stream()
                    .mapToLong(trip -> {
                        long outDelay = trip.getDelayOutMinutes() != null && trip.getDelayOutMinutes() > 0 ? trip.getDelayOutMinutes() : 0;
                        long inDelay = trip.getDelayInMinutes() != null && trip.getDelayInMinutes() > 0 ? trip.getDelayInMinutes() : 0;
                        return outDelay + inDelay;
                    })
                    .sum();
                return new DelayLeaderboardDTO(bus.getBusNo(), totalDelay);
            })
            .filter(dto -> dto.getTotalDelayMinutes() > 0)
            .sorted(Comparator.comparingLong(DelayLeaderboardDTO::getTotalDelayMinutes).reversed())
            .collect(Collectors.toList());
    }

    public List<DailyBusStatusDTO> getMissedBuses(LocalDate date) {
        List<DailyBusStatusDTO> comparisons = compareSchedule(date);
        return comparisons.stream()
            .filter(c -> "ABSENT".equals(c.getOverallStatus()) || "PARTIAL".equals(c.getOverallStatus()))
            .collect(Collectors.toList());
    }

    private void calculateOverallStatus(DailyBusStatusDTO busStatus) {
        boolean anyActivity = busStatus.getTrips().stream().anyMatch(t -> t.getActualOut() != null || t.getActualIn() != null);
        boolean anyMissed = busStatus.getTrips().stream().anyMatch(t -> "MISSED".equals(t.getStatus()) || "PARTIAL".equals(t.getStatus()));
        boolean anyLate = busStatus.getTrips().stream().anyMatch(t -> "LATE".equals(t.getStatus()));

        if (!anyActivity) {
            busStatus.setOverallStatus("ABSENT");
        } else if (anyMissed) {
            busStatus.setOverallStatus("PARTIAL");
        } else if (anyLate) {
            busStatus.setOverallStatus("LATE");
        } else {
            busStatus.setOverallStatus("ON_TIME");
        }
    }

    private void calculateTripStatus(TripComparisonDTO dto) {
        boolean outHappened = dto.getActualOut() != null;
        boolean inHappened = dto.getActualIn() != null;

        if (!outHappened && !inHappened) {
            dto.setStatus("MISSED");
            return;
        }
        if (!outHappened || !inHappened) {
            dto.setStatus("PARTIAL");
        }

        if (dto.getPlannedOut() != null && outHappened) {
            dto.setDelayOutMinutes(ChronoUnit.MINUTES.between(dto.getPlannedOut(), dto.getActualOut()));
        }
        if (dto.getPlannedIn() != null && inHappened) {
            dto.setDelayInMinutes(ChronoUnit.MINUTES.between(dto.getPlannedIn(), dto.getActualIn()));
        }

        if (dto.getStatus() == null) {
            boolean isDelayed = (dto.getDelayOutMinutes() != null && dto.getDelayOutMinutes() > 0) ||
                                (dto.getDelayInMinutes() != null && dto.getDelayInMinutes() > 0);
            if (isDelayed) {
                dto.setStatus("LATE");
            } else {
                dto.setStatus("ON_TIME");
            }
        }
    }
}
