package com.example.ReservationAppBackEnd.reservation.tools;

import java.time.LocalDateTime;

public class TimeRange {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}