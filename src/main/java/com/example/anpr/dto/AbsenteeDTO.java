package com.example.anpr.dto;

public class AbsenteeDTO {
    private String busNo;
    private long absentCount;

    public AbsenteeDTO(String busNo, long absentCount) {
        this.busNo = busNo;
        this.absentCount = absentCount;
    }

    // Getters and Setters
    public String getBusNo() { return busNo; }
    public void setBusNo(String busNo) { this.busNo = busNo; }

    public long getAbsentCount() { return absentCount; }
    public void setAbsentCount(long absentCount) { this.absentCount = absentCount; }
}
