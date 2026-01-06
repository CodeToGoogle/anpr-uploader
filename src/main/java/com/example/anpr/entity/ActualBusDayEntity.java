package com.example.anpr.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "actual_bus_day", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"bus_no", "date"})
})
public class ActualBusDayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_no", nullable = false)
    private String busNo;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "out_time")
    private LocalTime outTime;

    @Column(name = "in_time")
    private LocalTime inTime;

    @Column(name = "source_completed")
    private Boolean sourceCompleted = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBusNo() { return busNo; }
    public void setBusNo(String busNo) { this.busNo = busNo; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getOutTime() { return outTime; }
    public void setOutTime(LocalTime outTime) { this.outTime = outTime; }
    public LocalTime getInTime() { return inTime; }
    public void setInTime(LocalTime inTime) { this.inTime = inTime; }
    public Boolean getSourceCompleted() { return sourceCompleted; }
    public void setSourceCompleted(Boolean sourceCompleted) { this.sourceCompleted = sourceCompleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
