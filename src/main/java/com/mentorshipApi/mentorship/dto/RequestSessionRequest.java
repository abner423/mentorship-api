package com.mentorshipApi.mentorship.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class RequestSessionRequest {
    @NotNull
    private Long mentorId;

    @NotNull
    private LocalDateTime date;

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
