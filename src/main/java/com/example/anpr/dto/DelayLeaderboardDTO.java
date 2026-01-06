package com.example.anpr.dto;

public class DelayLeaderboardDTO {
    private String busNo;
    private long totalDelayMinutes;

    public DelayLeaderboardDTO(String busNo, long totalDelayMinutes) {
        this.busNo = busNo;
        this.totalDelayMinutes = totalDelayMinutes;
    }

    // Getters & Setters
    public String getBusNo() { return busNo; }
    public void setBusNo(String busNo) { this.busNo = busNo; }
    public long getTotalDelayMinutes() { return totalDelayMinutes; }
    public void setTotalDelayMinutes(long totalDelayMinutes) { this.totalDelayMinutes = totalDelayMinutes; }
}
