package com.example.anpr.dto;

import java.time.LocalTime;

public class TripComparisonDTO {
    private Integer tripNo;
    private String routeName;
    private String routeNo;
    private LocalTime plannedOut;
    private LocalTime actualOut;
    private Long delayOutMinutes;
    private LocalTime plannedIn;
    private LocalTime actualIn;
    private Long delayInMinutes;
    private String status;

    public Integer getTripNo() { return tripNo; }
    public void setTripNo(Integer tripNo) { this.tripNo = tripNo; }
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    public String getRouteNo() { return routeNo; }
    public void setRouteNo(String routeNo) { this.routeNo = routeNo; }
    public LocalTime getPlannedOut() { return plannedOut; }
    public void setPlannedOut(LocalTime plannedOut) { this.plannedOut = plannedOut; }
    public LocalTime getActualOut() { return actualOut; }
    public void setActualOut(LocalTime actualOut) { this.actualOut = actualOut; }
    public Long getDelayOutMinutes() { return delayOutMinutes; }
    public void setDelayOutMinutes(Long delayOutMinutes) { this.delayOutMinutes = delayOutMinutes; }
    public LocalTime getPlannedIn() { return plannedIn; }
    public void setPlannedIn(LocalTime plannedIn) { this.plannedIn = plannedIn; }
    public LocalTime getActualIn() { return actualIn; }
    public void setActualIn(LocalTime actualIn) { this.actualIn = actualIn; }
    public Long getDelayInMinutes() { return delayInMinutes; }
    public void setDelayInMinutes(Long delayInMinutes) { this.delayInMinutes = delayInMinutes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
