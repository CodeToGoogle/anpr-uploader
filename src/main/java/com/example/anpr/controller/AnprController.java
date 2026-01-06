package com.example.anpr.controller;

import com.example.anpr.service.AnprService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/anpr")
public class AnprController {

    @Autowired
    private AnprService anprService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadAnprFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {
        if (!"IN".equalsIgnoreCase(type) && !"OUT".equalsIgnoreCase(type)) {
            return ResponseEntity.badRequest().body("Type parameter must be 'IN' or 'OUT'");
        }
        Map<String, Object> result = anprService.uploadAnprEvents(file, type.toUpperCase());
        return ResponseEntity.ok(result);
    }
}
