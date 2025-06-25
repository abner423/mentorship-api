package com.mentorshipApi.mentorship.dto;

public class NextSessionResponse {
    private SessionResponse nextSession;

    public NextSessionResponse(SessionResponse sessionResponse) {
        this.nextSession = sessionResponse;
    }

    public NextSessionResponse(){}

    public SessionResponse getNextSession() {
        return nextSession;
    }

    public void setNextSession(SessionResponse nextSession) {
        this.nextSession = nextSession;
    }
}
