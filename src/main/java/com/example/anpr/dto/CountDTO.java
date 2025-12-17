package com.example.anpr.dto;

public class CountDTO {
    private String name;
    private long count;

    public CountDTO(String name, long count) {
        this.name = name;
        this.count = count;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
