package com.mentorshipApi.mentorship.service;

import com.mentorshipApi.mentorship.dto.*;
import com.mentorshipApi.mentorship.entity.Mentor;
import com.mentorshipApi.mentorship.entity.Session;
import com.mentorshipApi.mentorship.enums.SessionStatus;
import com.mentorshipApi.mentorship.exception.EntityNotFoundException;
import com.mentorshipApi.mentorship.exception.UserAlreadyExistsException;
import com.mentorshipApi.mentorship.repository.MentorRepository;
import com.mentorshipApi.mentorship.repository.SessionRepository;
import com.mentorshipApi.mentorship.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MentorService {
    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    SessionRepository sessionRepository;

    public void createMentor(CreateMentorRequest request) throws UserAlreadyExistsException {
        Optional<Mentor> mentor = mentorRepository.findByEmail(request.getEmail());

        if(mentor.isEmpty()) {
            mentorRepository.save(new Mentor(request.getName(), request.getEmail(), PasswordUtils.encryptPassword(request.getPassword())));
        } else {
            throw new UserAlreadyExistsException("A Mentor with this email already exists");
        }
    }

    public List<Session> getLastSessions(Long mentorId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        return sessionRepository
                .findByMentorIdAndStatusOrderByDateDesc(mentorId, SessionStatus.COMPLETED, pageable)
                .getContent();
    }

    public PaginatedResponse<MentorResponse> getMentorsPaginated(Pageable pageable) {
        Page<Mentor> studentsPage = mentorRepository.findAll(pageable);
        Page<MentorResponse> dtoPage = studentsPage.map(MentorResponse::fromEntity);
        return new PaginatedResponse<>(dtoPage);
    }

    public PaginatedResponse<SessionResponse> getSessionsToApprove(Long mentorId, Pageable pageable) {
        Page<Session> sessionPage = sessionRepository
                .findAllByMentorIdAndStatusOrderByDateDesc(mentorId, SessionStatus.PENDING, pageable);
        Page<SessionResponse> dtoPage = sessionPage.map(SessionResponse::fromEntity);
        return new PaginatedResponse<>(dtoPage);
    }

    public void changeSessionStatus(Long sessionId, SessionStatus newStatus) throws EntityNotFoundException,IllegalArgumentException {
        if (newStatus != SessionStatus.ACCEPTED && newStatus != SessionStatus.REJECTED) {
            throw new IllegalArgumentException("Only ACCEPTED or REJECTED are allowed");
        }

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + sessionId));

        session.setStatus(newStatus);
        sessionRepository.save(session);
    }
}
