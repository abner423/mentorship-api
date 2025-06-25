package com.mentorshipApi.mentorship.repository;

import com.mentorshipApi.mentorship.entity.Mentor;
import com.mentorshipApi.mentorship.entity.Session;
import com.mentorshipApi.mentorship.enums.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Page<Session> findByStudentIdAndStatusOrderByDateDesc(Long studentId, SessionStatus status, Pageable pageable);
    Optional<Session> findByStudentIdAndStatusOrderByDateDesc(Long studentId, SessionStatus status);

    Page<Session> findByMentorIdAndStatusOrderByDateDesc(Long mentorId, SessionStatus status, Pageable pageable);
    Page<Session> findAllByMentorIdAndStatusOrderByDateDesc(Long mentorId, SessionStatus status, Pageable pageable);
}
