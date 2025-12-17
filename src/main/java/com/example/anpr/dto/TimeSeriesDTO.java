package com.example.anpr.dto;

import java.time.LocalDateTime;

public class TimeSeriesDTO {
    private LocalDateTime time;
    private long count;

    public TimeSeriesDTO(LocalDateTime time, long count) {
        this.time = time;
        this.count = count;
    }

    // Getters and setters
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
