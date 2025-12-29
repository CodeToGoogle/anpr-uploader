package com.example.anpr.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class BusAvailabilityDTO {
    private String busNo;
    private LocalDate date;
    private LocalTime outTime;
    private LocalTime inTime;
    private String morningRoute;
    private String eveningRoute;

    public BusAvailabilityDTO(String busNo, LocalDate date, LocalTime outTime, LocalTime inTime, String morningRoute, String eveningRoute) {
        this.busNo = busNo;
        this.date = date;
        this.outTime = outTime;
        this.inTime = inTime;
        this.morningRoute = morningRoute;
        this.eveningRoute = eveningRoute;
    }

    // Getters and Setters
    public String getBusNo() { return busNo; }
    public void setBusNo(String busNo) { this.busNo = busNo; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getOutTime() { return outTime; }
    public void setOutTime(LocalTime outTime) { this.outTime = outTime; }

    public LocalTime getInTime() { return inTime; }
    public void setInTime(LocalTime inTime) { this.inTime = inTime; }

    public String getMorningRoute() { return morningRoute; }
    public void setMorningRoute(String morningRoute) { this.morningRoute = morningRoute; }

    public String getEveningRoute() { return eveningRoute; }
    public void setEveningRoute(String eveningRoute) { this.eveningRoute = eveningRoute; }
}
