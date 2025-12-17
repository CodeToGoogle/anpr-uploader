package com.example.anpr.dto;

public class JunctionCountDTO {
    private String junctionName;
    private long count;

    public JunctionCountDTO(String junctionName, long count) {
        this.junctionName = junctionName;
        this.count = count;
    }

    // Getters and setters
    public String getJunctionName() {
        return junctionName;
    }

    public void setJunctionName(String junctionName) {
        this.junctionName = junctionName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
