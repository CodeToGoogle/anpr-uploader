package com.example.anpr.dto;

import java.time.LocalDate;

public class ShiftSummaryDTO {
    private LocalDate date;
    private long morningOnly;
    private long eveningOnly;
    private long bothShifts;
    private long absent;

    public ShiftSummaryDTO(LocalDate date, long morningOnly, long eveningOnly, long bothShifts, long absent) {
        this.date = date;
        this.morningOnly = morningOnly;
        this.eveningOnly = eveningOnly;
        this.bothShifts = bothShifts;
        this.absent = absent;
    }

    // Getters and Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public long getMorningOnly() { return morningOnly; }
    public void setMorningOnly(long morningOnly) { this.morningOnly = morningOnly; }

    public long getEveningOnly() { return eveningOnly; }
    public void setEveningOnly(long eveningOnly) { this.eveningOnly = eveningOnly; }

    public long getBothShifts() { return bothShifts; }
    public void setBothShifts(long bothShifts) { this.bothShifts = bothShifts; }

    public long getAbsent() { return absent; }
    public void setAbsent(long absent) { this.absent = absent; }
}
