package com.mentorshipApi.mentorship.dto;

import com.mentorshipApi.mentorship.enums.SessionStatus;
import jakarta.validation.constraints.NotNull;

public class ChangeSessionStatus {
    @NotNull
    private SessionStatus newStatus;

    public SessionStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(SessionStatus newStatus) {
        this.newStatus = newStatus;
    }
}
