package com.example.anpr.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "outshedding")
public class OutsheddingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_no", nullable = false)
    private String busNo;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "out_time")
    private LocalTime outTime;

    @Column(name = "in_time")
    private LocalTime inTime;

    @Column(name = "morning_route")
    private String morningRoute;

    @Column(name = "morning_route_no")
    private String morningRouteNo;

    @Column(name = "evening_route")
    private String eveningRoute;

    @Column(name = "evening_route_no")
    private String eveningRouteNo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters

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

    public String getMorningRoute() { return morningRoute; }
    public void setMorningRoute(String morningRoute) { this.morningRoute = morningRoute; }

    public String getMorningRouteNo() { return morningRouteNo; }
    public void setMorningRouteNo(String morningRouteNo) { this.morningRouteNo = morningRouteNo; }

    public String getEveningRoute() { return eveningRoute; }
    public void setEveningRoute(String eveningRoute) { this.eveningRoute = eveningRoute; }

    public String getEveningRouteNo() { return eveningRouteNo; }
    public void setEveningRouteNo(String eveningRouteNo) { this.eveningRouteNo = eveningRouteNo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
