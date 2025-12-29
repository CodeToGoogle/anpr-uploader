package com.example.anpr.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bus_master")
public class BusMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_no", unique = true, nullable = false)
    private String busNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }
}
