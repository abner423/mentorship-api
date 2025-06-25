package com.mentorshipApi.mentorship.dto;

import com.mentorshipApi.mentorship.entity.Session;

import java.time.LocalDateTime;

public class SessionResponse {
    private Long id;
    private LocalDateTime date;
    private MentorResponse mentor;


    public SessionResponse(Long id, LocalDateTime date, MentorResponse mentor) {
        this.id = id;
        this.date = date;
        this.mentor = mentor;
    }

    public static SessionResponse fromEntity(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getDate(),
                new MentorResponse(session.getMentor().getId(), session.getMentor().getName(), session.getMentor().getEmail())
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public MentorResponse getMentor() {
        return mentor;
    }

    public void setMentor(MentorResponse mentor) {
        this.mentor = mentor;
    }
}
