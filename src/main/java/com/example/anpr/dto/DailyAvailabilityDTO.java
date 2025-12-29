package com.example.anpr.dto;

import java.time.LocalDate;

public class DailyAvailabilityDTO {
    private LocalDate date;
    private long totalRegistered;
    private long present;
    private long absent;

    public DailyAvailabilityDTO(LocalDate date, long totalRegistered, long present, long absent) {
        this.date = date;
        this.totalRegistered = totalRegistered;
        this.present = present;
        this.absent = absent;
    }

    // Getters and Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public long getTotalRegistered() { return totalRegistered; }
    public void setTotalRegistered(long totalRegistered) { this.totalRegistered = totalRegistered; }

    public long getPresent() { return present; }
    public void setPresent(long present) { this.present = present; }

    public long getAbsent() { return absent; }
    public void setAbsent(long absent) { this.absent = absent; }
}
