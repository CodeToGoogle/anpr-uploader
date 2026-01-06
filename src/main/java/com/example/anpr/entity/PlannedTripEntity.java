package com.example.anpr.entity;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "planned_trips", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"bus_no", "trip_no"})
})
public class PlannedTripEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_no", nullable = false)
    private String busNo;

    @Column(name = "trip_no", nullable = false)
    private Integer tripNo;

    @Column(name = "planned_out")
    private LocalTime plannedOut;

    @Column(name = "planned_in")
    private LocalTime plannedIn;

    @Column(name = "route_name")
    private String routeName;

    @Column(name = "route_no")
    private String routeNo;

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
    public Integer getTripNo() { return tripNo; }
    public void setTripNo(Integer tripNo) { this.tripNo = tripNo; }
    public LocalTime getPlannedOut() { return plannedOut; }
    public void setPlannedOut(LocalTime plannedOut) { this.plannedOut = plannedOut; }
    public LocalTime getPlannedIn() { return plannedIn; }
    public void setPlannedIn(LocalTime plannedIn) { this.plannedIn = plannedIn; }
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    public String getRouteNo() { return routeNo; }
    public void setRouteNo(String routeNo) { this.routeNo = routeNo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
