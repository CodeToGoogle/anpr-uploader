package com.example.anpr;

import com.example.anpr.controller.BusController;
import com.example.anpr.dto.BusAvailabilityDTO;
import com.example.anpr.dto.BusHistoryDTO;
import com.example.anpr.dto.CountDTO;
import com.example.anpr.service.BusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusController.class)
public class BusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusService busService;

    @Test
    public void testUploadBusMaster() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "Buses.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "content".getBytes());
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");

        given(busService.uploadBusMaster(any())).willReturn(result);

        mockMvc.perform(multipart("/api/bus/upload-master").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    public void testUploadOutshedding() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "Outshedding.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "content".getBytes());
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");

        given(busService.uploadOutshedding(any())).willReturn(result);

        mockMvc.perform(multipart("/api/outshedding/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    public void testGetBusAvailability() throws Exception {
        LocalDate date = LocalDate.of(2023, 10, 27);
        BusAvailabilityDTO dto = new BusAvailabilityDTO("KA-01-F-1234", date, LocalTime.of(6, 30), LocalTime.of(20, 15), "Route A", "Route B");

        given(busService.getBusAvailability(date)).willReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/outshedding/availability").param("date", "2023-10-27"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].busNo").value("KA-01-F-1234"));
    }

    @Test
    public void testGetMostFrequentMorningRoutes() throws Exception {
        CountDTO dto = new CountDTO("Route A", 15);

        given(busService.getMostFrequentMorningRoutes()).willReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/outshedding/routes/morning"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Route A"))
                .andExpect(jsonPath("$[0].count").value(15));
    }

    @Test
    public void testGetMostFrequentEveningRoutes() throws Exception {
        CountDTO dto = new CountDTO("Route B", 12);

        given(busService.getMostFrequentEveningRoutes()).willReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/outshedding/routes/evening"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Route B"))
                .andExpect(jsonPath("$[0].count").value(12));
    }

    @Test
    public void testGetMissingBuses() throws Exception {
        LocalDate date = LocalDate.of(2023, 10, 27);
        given(busService.getMissingBuses(date)).willReturn(Arrays.asList("KA-01-F-5678", "KA-01-F-9012"));

        mockMvc.perform(get("/api/outshedding/missing").param("date", "2023-10-27"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("KA-01-F-5678"))
                .andExpect(jsonPath("$[1]").value("KA-01-F-9012"));
    }

    @Test
    public void testGetBusHistory() throws Exception {
        String busNo = "KA-01-F-1234";
        BusHistoryDTO dto = new BusHistoryDTO(LocalDate.of(2023, 10, 27), LocalTime.of(6, 30), LocalTime.of(20, 15), "Route A", "Route B");

        given(busService.getBusHistory(busNo)).willReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/outshedding/bus/{busNo}", busNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2023-10-27"));
    }
}
