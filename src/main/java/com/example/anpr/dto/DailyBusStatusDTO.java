package com.example.anpr.dto;

import java.util.List;

public class DailyBusStatusDTO {
    private String busNo;
    private String overallStatus;
    private List<TripComparisonDTO> trips;

    public String getBusNo() { return busNo; }
    public void setBusNo(String busNo) { this.busNo = busNo; }
    public String getOverallStatus() { return overallStatus; }
    public void setOverallStatus(String overallStatus) { this.overallStatus = overallStatus; }
    public List<TripComparisonDTO> getTrips() { return trips; }
    public void setTrips(List<TripComparisonDTO> trips) { this.trips = trips; }
}
