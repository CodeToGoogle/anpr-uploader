package com.example.anpr.service;

import com.example.anpr.entity.EventEntity;
import com.example.anpr.repository.EventRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class EventExcelService {

    @Autowired
    private EventRepository eventRepository;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss");

    /**
     * Imports events from uploaded Excel file.
     * Assumes first sheet; row 0 is header; Details column is column index 1 (B).
     *
     * Returns map with stats.
     */
    public Map<String, Object> importEvents(MultipartFile file) {
        int inserted = 0;
        int duplicates = 0;
        int total = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) throw new RuntimeException("No sheets found in workbook");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                total++;

                Cell detailsCell = row.getCell(1); // column B (0-based index)
                if (detailsCell == null) continue;

                String details = getCellString(detailsCell);
                EventEntity event = parseDetails(details);

                try {
                    eventRepository.save(event);
                    inserted++;
                } catch (DataIntegrityViolationException dex) {
                    // likely unique constraint violation
                    duplicates++;
                } catch (Exception ex) {
                    // log & continue
                    System.err.println("Error saving row " + i + ": " + ex.getMessage());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process Excel file: " + e.getMessage(), e);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total_rows", total);
        result.put("inserted", inserted);
        result.put("duplicates", duplicates);
        result.put("status", "success");
        return result;
    }

    private String getCellString(Cell c) {
        if (c == null) return null;
        if (c.getCellType() == CellType.STRING) return c.getStringCellValue();
        if (c.getCellType() == CellType.NUMERIC) return String.valueOf(c.getNumericCellValue());
        return c.toString();
    }

    private EventEntity parseDetails(String details) {
        if (details == null) return new EventEntity();
        Map<String, String> map = new HashMap<>();
        String[] lines = details.split("\\r?\\n");
        String lastKey = null;
        for (String line : lines) {
            if (line == null) continue;
            line = line.trim();
            if (line.isEmpty()) continue;

            if (!line.contains(":")) {
                // continuation of previous value (e.g., multi-line message)
                if (lastKey != null) {
                    map.put(lastKey, map.getOrDefault(lastKey, "") + " " + line);
                }
                continue;
            }

            String[] parts = line.split(":", 2);
            String key = parts[0].trim();
            String val = parts.length > 1 ? parts[1].trim() : "";
            map.put(key, val);
            lastKey = key;
        }

        EventEntity e = new EventEntity();
        e.setJunctionName(map.getOrDefault("Junction", null));
        e.setCameraName(map.getOrDefault("Camera", null));
        e.setEventType(map.getOrDefault("Event", null));
        e.setStatus(map.getOrDefault("Status", null));
        e.setPriority(map.getOrDefault("Priority", null));
        e.setMessage(map.getOrDefault("Message", null));
        // Action -> Acknowledged by may be nested; handle both keys
        String ack = map.get("Acknowledged by");
        if (ack == null && map.containsKey("Action")) {
            // try to find "Acknowledged by" inside Action value
            String actionVal = map.get("Action");
            if (actionVal != null && actionVal.contains("Acknowledged by")) {
                String[] parts = actionVal.split("Acknowledged by",2);
                ack = parts.length>1 ? parts[1].replace(":"," ").trim() : null;
            }
        }
        e.setAcknowledgedBy(ack);
        e.setColor(map.getOrDefault("Color", null));
        String plate = map.getOrDefault("Number Captured", map.getOrDefault("Number Captured:", null));
        if (plate != null) plate = plate.replace("'", "").trim();
        e.setPlateNumber(plate);
        String sp = map.getOrDefault("Speed", null);
        e.setSpeed(parseSpeed(sp));

        String dt = map.getOrDefault("Date & Time", null);
        if (dt != null) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(dt, DTF);
                e.setEventTime(ldt);
            } catch (Exception ex) {
                // try alternative formats or leave null
                System.err.println("Failed to parse date: " + dt);
            }
        }

        return e;
    }

    private Integer parseSpeed(String s) {
        if (s == null) return null;
        String cleaned = s.replace("kmph","").replace("'","").replace("km/h","" ).trim();
        try {
            return Integer.parseInt(cleaned.split(" ")[0].trim());
        } catch (Exception ex) {
            return null;
        }
    }
}
