package com.example.anpr.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "actual_events", indexes = {
    @Index(name = "idx_actual_events_bus_no_event_time", columnList = "bus_no, event_time"),
    @Index(name = "idx_actual_events_direction", columnList = "direction")
})
public class ActualEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_no", nullable = false)
    private String busNo;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "junction")
    private String junction;

    @Column(name = "camera")
    private String camera;

    @Column(name = "speed")
    private Integer speed;

    @Column(name = "direction", nullable = false) // IN or OUT
    private String direction;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (direction == null && camera != null) {
            if (camera.toLowerCase().contains("out")) {
                direction = "OUT";
            } else if (camera.toLowerCase().contains("in")) {
                direction = "IN";
            } else {
                direction = "UNKNOWN";
            }
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBusNo() { return busNo; }
    public void setBusNo(String busNo) { this.busNo = busNo; }
    public LocalDateTime getEventTime() { return eventTime; }
    public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }
    public String getJunction() { return junction; }
    public void setJunction(String junction) { this.junction = junction; }
    public String getCamera() { return camera; }
    public void setCamera(String camera) { this.camera = camera; }
    public Integer getSpeed() { return speed; }
    public void setSpeed(Integer speed) { this.speed = speed; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
