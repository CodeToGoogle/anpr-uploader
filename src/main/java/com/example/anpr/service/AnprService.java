package com.example.anpr.service;

import com.example.anpr.entity.ActualEventEntity;
import com.example.anpr.repository.ActualEventRepository;
import com.example.anpr.repository.BusMasterRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AnprService {

    @Autowired
    private ActualEventRepository actualEventRepository;

    @Autowired
    private BusMasterRepository busMasterRepository;
    
    @Autowired
    private DataProcessingService dataProcessingService;

    private static final DateTimeFormatter FORMATTER_1 = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_3 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Map<String, Object> uploadAnprEvents(MultipartFile file, String type) {
        int inserted = 0;
        int skipped = 0;
        int total = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Set<String> busDateKeys = new HashSet<>();
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String originalBusNo = getCellString(row.getCell(5));
                if (originalBusNo == null || originalBusNo.isEmpty()) continue;
                total++;

                // Normalize the bus number
                String normalizedBusNo = normalizeBusNo(originalBusNo);

                if (!busMasterRepository.existsByBusNo(normalizedBusNo)) {
                    System.out.println("Skipping row " + (i + 1) + ": Bus No '" + originalBusNo + "' (normalized to '" + normalizedBusNo + "') not found in master list.");
                    skipped++;
                    continue;
                }

                ActualEventEntity event = new ActualEventEntity();
                event.setBusNo(normalizedBusNo); // Save the normalized version
                event.setDirection(type.toUpperCase());

                event.setJunction(getCellString(row.getCell(1)));
                event.setCamera(getCellString(row.getCell(2)));
                
                LocalDateTime eventTime = getCellDateTime(row.getCell(4));
                if (eventTime == null) {
                    System.err.println("Skipping row " + i + ": Could not parse date from cell value: " + getCellString(row.getCell(4)));
                    skipped++;
                    continue;
                }
                event.setEventTime(eventTime);
                
                event.setSpeed(getCellInt(row.getCell(7)));

                actualEventRepository.save(event);
                inserted++;
                
                busDateKeys.add(normalizedBusNo + ":" + eventTime.toLocalDate());
            }
            
            for (String key : busDateKeys) {
                String[] parts = key.split(":", 2);
                dataProcessingService.processAndSummarizeDay(parts[0], java.time.LocalDate.parse(parts[1]));
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to process ANPR Excel file: " + e.getMessage(), e);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total_rows", total);
        result.put("inserted", inserted);
        result.put("skipped_invalid_bus", skipped);
        result.put("status", "success");
        return result;
    }

    private String normalizeBusNo(String busNo) {
        if (busNo == null) return null;
        // Convert to uppercase and remove all non-alphanumeric characters
        return busNo.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    private String getCellString(Cell c) {
        if (c == null) return null;
        c.setCellType(CellType.STRING);
        String value = c.getStringCellValue().trim();
        return value.isEmpty() ? null : value;
    }

    private LocalDateTime getCellDateTime(Cell c) {
        if (c == null) return null;

        if (c.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(c)) {
            return c.getLocalDateTimeCellValue();
        }

        if (c.getCellType() == CellType.STRING) {
            String dateStr = c.getStringCellValue().trim();
            if (dateStr.isEmpty()) return null;

            try {
                return LocalDateTime.parse(dateStr, FORMATTER_1);
            } catch (Exception e1) {
                try {
                    return LocalDateTime.parse(dateStr, FORMATTER_2);
                } catch (Exception e2) {
                    try {
                        return LocalDateTime.parse(dateStr, FORMATTER_3);
                    } catch (Exception e3) {
                        return null;
                    }
                }
            }
        }
        
        return null;
    }

    private Integer getCellInt(Cell c) {
        if (c == null || c.getCellType() != CellType.NUMERIC) return null;
        return (int) c.getNumericCellValue();
    }
}
