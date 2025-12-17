package com.example.anpr.controller;

import com.example.anpr.service.EventExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class UploadController {

    @Autowired
    private EventExcelService eventExcelService;

    @PostMapping("/upload-events")
    public ResponseEntity<?> uploadEvents(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = eventExcelService.importEvents(file);
        return ResponseEntity.ok(result);
    }
}
