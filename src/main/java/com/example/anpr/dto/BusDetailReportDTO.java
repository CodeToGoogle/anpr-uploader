package com.example.anpr.dto;

import com.example.anpr.entity.ActualEventEntity;
import java.util.List;

public class BusDetailReportDTO {
    private DailyBusStatusDTO summary;
    private List<ActualEventEntity> actualEvents;

    public DailyBusStatusDTO getSummary() { return summary; }
    public void setSummary(DailyBusStatusDTO summary) { this.summary = summary; }
    public List<ActualEventEntity> getActualEvents() { return actualEvents; }
    public void setActualEvents(List<ActualEventEntity> actualEvents) { this.actualEvents = actualEvents; }
}
