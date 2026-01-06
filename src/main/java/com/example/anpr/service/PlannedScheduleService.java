package com.example.anpr.service;

import com.example.anpr.entity.PlannedTripEntity;
import com.example.anpr.repository.BusMasterRepository;
import com.example.anpr.repository.PlannedTripRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class PlannedScheduleService {

    @Autowired
    private BusMasterRepository busMasterRepository;

    @Autowired
    private PlannedTripRepository plannedTripRepository;

    public Map<String, Object> uploadPlannedTrips(MultipartFile file) {
        int uploaded = 0;
        int skipped = 0;
        
        Map<String, Integer> tripCounters = new HashMap<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            
            plannedTripRepository.deleteAllInBatch();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String rawBusNo = getCellString(row.getCell(1));
                
                if (rawBusNo == null || rawBusNo.isEmpty()) {
                    continue;
                }
                
                String busNo = normalizeBusNo(rawBusNo);

                if (!busMasterRepository.existsByBusNo(busNo)) {
                    System.out.println("Skipping row " + (i + 1) + ": Bus No '" + rawBusNo + "' (normalized: " + busNo + ") not found in master list.");
                    skipped++;
                    continue;
                }

                int lastTripNo = tripCounters.computeIfAbsent(busNo, bn -> 
                    plannedTripRepository.findMaxTripNoByBusNo(bn).orElse(0)
                );
                int nextTripNo = lastTripNo + 1;
                tripCounters.put(busNo, nextTripNo);

                PlannedTripEntity trip = new PlannedTripEntity();
                trip.setBusNo(busNo);
                trip.setTripNo(nextTripNo);
                
                trip.setPlannedOut(getCellTime(row.getCell(2)));
                trip.setPlannedIn(getCellTime(row.getCell(3)));
                
                trip.setRouteName(getCellString(row.getCell(4)));
                trip.setRouteNo(normalizeRouteNo(getCellString(row.getCell(5))));

                plannedTripRepository.save(trip);
                uploaded++;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process Planned Schedule Excel: " + e.getMessage(), e);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("uploaded", uploaded);
        result.put("skipped", skipped);
        result.put("message", "Schedule imported successfully");
        return result;
    }

    private String getCellString(Cell cell) {
        if (cell == null) return null;
        cell.setCellType(CellType.STRING);
        String value = cell.getStringCellValue().trim();
        return value.isEmpty() ? null : value;
    }

    private LocalTime getCellTime(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalTime();
            }
            try {
                return DateUtil.getJavaDate(cell.getNumericCellValue()).toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalTime();
            } catch (Exception e) {
            }
        }

        cell.setCellType(CellType.STRING);
        String timeStr = cell.getStringCellValue().trim();
        return parseTimeWithAMPM(timeStr);
    }

    private LocalTime parseTimeWithAMPM(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        
        String cleaned = timeStr.toUpperCase().replaceAll("\\s+", " ").trim();
        
        if (cleaned.matches(".*\\d[AP]M$")) {
            cleaned = cleaned.replace("AM", " AM").replace("PM", " PM");
        }

        try {
            DateTimeFormatter ampmFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
            return LocalTime.parse(cleaned, ampmFormatter);
        } catch (Exception e1) {
            try {
                DateTimeFormatter singleDigitHour = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
                return LocalTime.parse(cleaned, singleDigitHour);
            } catch (Exception e2) {
                try {
                    return LocalTime.parse(cleaned);
                } catch (Exception e3) {
                    System.err.println("Could not parse time: " + timeStr);
                    return null;
                }
            }
        }
    }
    
    private String normalizeRouteNo(String routeNoStr) {
        if (routeNoStr == null) return null;
        return routeNoStr.replaceAll("[^0-9]", "").trim();
    }
    
    private String normalizeBusNo(String busNo) {
        if (busNo == null) return null;
        return busNo.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }
}
