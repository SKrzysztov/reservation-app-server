package com.example.ReservationAppBackEnd.reservation.tools;

import java.time.LocalDateTime;

public class AvailableTimeRange {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public AvailableTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public long getDurationInMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }
}