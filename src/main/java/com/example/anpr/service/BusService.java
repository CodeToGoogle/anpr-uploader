package com.example.anpr.service;

import com.example.anpr.dto.*;
import com.example.anpr.entity.BusMasterEntity;
import com.example.anpr.entity.OutsheddingEntity;
import com.example.anpr.repository.BusMasterRepository;
import com.example.anpr.repository.OutsheddingRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusService {

    @Autowired
    private BusMasterRepository busMasterRepository;

    @Autowired
    private OutsheddingRepository outsheddingRepository;

    public Map<String, Object> uploadBusMaster(MultipartFile file) {
        int inserted = 0;
        int duplicates = 0;
        int total = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header

                Cell busNoCell = row.getCell(1); // Assuming Bus No is in 2nd column (index 1)
                if (busNoCell == null) continue;

                String busNo = getCellString(busNoCell);
                if (busNo == null || busNo.isEmpty()) continue;
                total++;

                if (busMasterRepository.existsByBusNo(busNo)) {
                    duplicates++;
                } else {
                    BusMasterEntity entity = new BusMasterEntity();
                    entity.setBusNo(busNo);
                    busMasterRepository.save(entity);
                    inserted++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to process Bus Master Excel: " + e.getMessage(), e);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total_rows", total);
        result.put("inserted", inserted);
        result.put("duplicates", duplicates);
        result.put("status", "success");
        return result;
    }

    public Map<String, Object> uploadOutshedding(MultipartFile file) {
        int inserted = 0;
        int skipped = 0;
        int total = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String busNo = getCellString(getSafeCell(row, 1));
                
                if (busNo == null || busNo.isEmpty()) continue;
                total++;

                if (!busMasterRepository.existsByBusNo(busNo)) {
                    skipped++;
                    continue;
                }

                OutsheddingEntity entity = new OutsheddingEntity();
                entity.setBusNo(busNo);
                entity.setDate(LocalDate.now()); 

                entity.setOutTime(parseTime(getCellString(getSafeCell(row, 2))));
                entity.setInTime(parseTime(getCellString(getSafeCell(row, 3))));
                entity.setMorningRoute(getCellString(getSafeCell(row, 4)));
                entity.setMorningRouteNo(getCellString(getSafeCell(row, 5)));

                entity.setEveningRoute(getCellString(getSafeCell(row, 11)));
                entity.setEveningRouteNo(getCellString(getSafeCell(row, 12)));

                outsheddingRepository.save(entity);
                inserted++;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process Outshedding Excel: " + e.getMessage(), e);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total_rows", total);
        result.put("inserted", inserted);
        result.put("skipped_invalid_bus", skipped);
        result.put("status", "success");
        return result;
    }

    private Cell getSafeCell(Row row, int colIndex) {
        if (colIndex < 0) return null;
        return row.getCell(colIndex);
    }

    private String getCellString(Cell c) {
        if (c == null) return null;
        if (c.getCellType() == CellType.STRING) {
            String val = c.getStringCellValue().trim();
            return val.isEmpty() ? null : val;
        }
        if (c.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(c)) {
                return c.getLocalDateTimeCellValue().toLocalTime().toString();
            }
            return String.valueOf((long) c.getNumericCellValue());
        }
        String val = c.toString().trim();
        return val.isEmpty() ? null : val;
    }

    private LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) return null;
        try {
            return LocalTime.parse(timeStr);
        } catch (Exception e) {
            return null;
        }
    }

    public DailyAvailabilityDTO getDailyAvailability(LocalDate date) {
        long totalRegistered = busMasterRepository.count();
        long present = outsheddingRepository.countByDate(date);
        long absent = totalRegistered - present;
        return new DailyAvailabilityDTO(date, totalRegistered, present, absent);
    }

    public ShiftSummaryDTO getShiftSummary(LocalDate date) {
        long morningOnly = outsheddingRepository.countMorningOnly(date);
        long eveningOnly = outsheddingRepository.countEveningOnly(date);
        long bothShifts = outsheddingRepository.countBothShifts(date);
        long totalRegistered = busMasterRepository.count();
        long present = morningOnly + eveningOnly + bothShifts;
        long absent = totalRegistered - present;
        return new ShiftSummaryDTO(date, morningOnly, eveningOnly, bothShifts, absent);
    }

    public List<String> getMissingBuses(LocalDate date) {
        return outsheddingRepository.findMissingBuses(date);
    }

    public List<BusHistoryDTO> getBusHistory(String busNo) {
        return outsheddingRepository.findByBusNoOrderByDateDesc(busNo).stream()
                .map(e -> new BusHistoryDTO(e.getDate(), e.getOutTime(), e.getInTime(), e.getMorningRoute(), e.getEveningRoute()))
                .collect(Collectors.toList());
    }

    public List<AbsenteeDTO> getFrequentAbsentees(int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        List<Object[]> rawResults = outsheddingRepository.findFrequentAbsenteesRaw(startDate);
        return rawResults.stream()
                .map(row -> {
                    String busNo = (String) row[0];
                    long absentCount = ((Number) row[1]).longValue();
                    return new AbsenteeDTO(busNo, absentCount);
                })
                .collect(Collectors.toList());
    }
}
