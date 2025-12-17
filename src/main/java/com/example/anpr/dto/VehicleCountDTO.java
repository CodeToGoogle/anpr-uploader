package com.example.anpr.dto;

public class VehicleCountDTO {
    private String plateNumber;
    private long count;

    public VehicleCountDTO(String plateNumber, long count) {
        this.plateNumber = plateNumber;
        this.count = count;
    }

    // Getters and setters
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
