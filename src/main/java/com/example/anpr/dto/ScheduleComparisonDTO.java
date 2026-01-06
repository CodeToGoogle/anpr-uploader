package com.example.anpr.dto;

import java.time.LocalTime;

public class ScheduleComparisonDTO {
    private String busNo;
    private String status;

    // Morning
    private LocalTime morningOutPlanned;
    private LocalTime morningOutActual;
    private Long morningOutDelayMinutes;

    private LocalTime morningInPlanned;
    private LocalTime morningInActual;
    private Long morningInDelayMinutes;

    // Evening
    private LocalTime eveningOutPlanned;
    private LocalTime eveningOutActual;
    private Long eveningOutDelayMinutes;

    private LocalTime eveningInPlanned;
    private LocalTime eveningInActual;
    private Long eveningInDelayMinutes;

    // Getters and Setters
    public String getBusNo() { return busNo; }
    public void setBusNo(String busNo) { this.busNo = busNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalTime getMorningOutPlanned() { return morningOutPlanned; }
    public void setMorningOutPlanned(LocalTime morningOutPlanned) { this.morningOutPlanned = morningOutPlanned; }
    public LocalTime getMorningOutActual() { return morningOutActual; }
    public void setMorningOutActual(LocalTime morningOutActual) { this.morningOutActual = morningOutActual; }
    public Long getMorningOutDelayMinutes() { return morningOutDelayMinutes; }
    public void setMorningOutDelayMinutes(Long morningOutDelayMinutes) { this.morningOutDelayMinutes = morningOutDelayMinutes; }
    public LocalTime getMorningInPlanned() { return morningInPlanned; }
    public void setMorningInPlanned(LocalTime morningInPlanned) { this.morningInPlanned = morningInPlanned; }
    public LocalTime getMorningInActual() { return morningInActual; }
    public void setMorningInActual(LocalTime morningInActual) { this.morningInActual = morningInActual; }
    public Long getMorningInDelayMinutes() { return morningInDelayMinutes; }
    public void setMorningInDelayMinutes(Long morningInDelayMinutes) { this.morningInDelayMinutes = morningInDelayMinutes; }
    public LocalTime getEveningOutPlanned() { return eveningOutPlanned; }
    public void setEveningOutPlanned(LocalTime eveningOutPlanned) { this.eveningOutPlanned = eveningOutPlanned; }
    public LocalTime getEveningOutActual() { return eveningOutActual; }
    public void setEveningOutActual(LocalTime eveningOutActual) { this.eveningOutActual = eveningOutActual; }
    public Long getEveningOutDelayMinutes() { return eveningOutDelayMinutes; }
    public void setEveningOutDelayMinutes(Long eveningOutDelayMinutes) { this.eveningOutDelayMinutes = eveningOutDelayMinutes; }
    public LocalTime getEveningInPlanned() { return eveningInPlanned; }
    public void setEveningInPlanned(LocalTime eveningInPlanned) { this.eveningInPlanned = eveningInPlanned; }
    public LocalTime getEveningInActual() { return eveningInActual; }
    public void setEveningInActual(LocalTime eveningInActual) { this.eveningInActual = eveningInActual; }
    public Long getEveningInDelayMinutes() { return eveningInDelayMinutes; }
    public void setEveningInDelayMinutes(Long eveningInDelayMinutes) { this.eveningInDelayMinutes = eveningInDelayMinutes; }
}
