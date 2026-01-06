package com.example.anpr.service;

import com.example.anpr.entity.BusMasterEntity;
import com.example.anpr.repository.BusMasterRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class BusService {

    @Autowired
    private BusMasterRepository busMasterRepository;

    public Map<String, Object> uploadBusMaster(MultipartFile file) {
        int inserted = 0;
        int duplicates = 0;
        int total = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            // Clear existing master data to ensure the new file is the single source of truth
            busMasterRepository.deleteAllInBatch();

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header

                Cell busNoCell = row.getCell(1);
                if (busNoCell == null) continue;

                String rawBusNo = getCellString(busNoCell);
                if (rawBusNo == null || rawBusNo.isEmpty()) continue;
                total++;

                // Normalize the bus number before checking and saving
                String normalizedBusNo = normalizeBusNo(rawBusNo);

                if (busMasterRepository.existsByBusNo(normalizedBusNo)) {
                    duplicates++;
                } else {
                    BusMasterEntity entity = new BusMasterEntity();
                    entity.setBusNo(normalizedBusNo); // Save the clean, normalized version
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

    private String getCellString(Cell c) {
        if (c == null) return null;
        c.setCellType(CellType.STRING);
        String value = c.getStringCellValue().trim();
        return value.isEmpty() ? null : value;
    }
    
    private String normalizeBusNo(String busNo) {
        if (busNo == null) return null;
        // Convert to uppercase and remove all non-alphanumeric characters
        return busNo.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }
}
