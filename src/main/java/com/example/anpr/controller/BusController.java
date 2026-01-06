package com.example.anpr.controller;

import com.example.anpr.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class BusController {

    @Autowired
    private BusService busService;

    @PostMapping("/bus/upload-master")
    public ResponseEntity<?> uploadBusMaster(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = busService.uploadBusMaster(file);
        return ResponseEntity.ok(result);
    }
}
