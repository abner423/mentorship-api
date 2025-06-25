package com.mentorshipApi.mentorship.service;

import com.mentorshipApi.mentorship.dto.*;
import com.mentorshipApi.mentorship.entity.Mentor;
import com.mentorshipApi.mentorship.entity.Session;
import com.mentorshipApi.mentorship.entity.Student;
import com.mentorshipApi.mentorship.enums.SessionStatus;
import com.mentorshipApi.mentorship.exception.UserAlreadyExistsException;
import com.mentorshipApi.mentorship.repository.MentorRepository;
import com.mentorshipApi.mentorship.repository.SessionRepository;
import com.mentorshipApi.mentorship.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    SessionRepository sessionRepository;

    public PaginatedResponse<GetStudentResponse> getStudentsPaginated(Pageable pageable) {
        Page<Student> studentsPage = studentRepository.findAll(pageable);
        Page<GetStudentResponse> dtoPage = studentsPage.map(GetStudentResponse::fromEntity);
        return new PaginatedResponse<>(dtoPage);
    }

    public void createStudent(CreateStudentRequest request) throws UserAlreadyExistsException {
        Optional<Student> student = studentRepository.findByEmail(request.getEmail());

        if(student.isEmpty()) {
            studentRepository.save(new Student(request.getName(), request.getEmail(), request.getPassword()));
        } else {
            throw new UserAlreadyExistsException("A student with this email already exists");
        }
    }

    public List<Session> getLastSessions(Long studentId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        return sessionRepository
                .findByStudentIdAndStatusOrderByDateDesc(studentId, SessionStatus.COMPLETED, pageable)
                .getContent();
    }

    public NextSessionResponse getNextSession(Long studentId) {
        Optional<Session> optionalSession = sessionRepository
                .findByStudentIdAndStatusOrderByDateDesc(studentId, SessionStatus.ACCEPTED);

        return optionalSession.map(session -> new NextSessionResponse(SessionResponse.fromEntity(session))).orElseGet(NextSessionResponse::new);
    }

    public void requestSessionToMentor(Long studentId, RequestSessionRequest request) throws EntityNotFoundException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<Student> studentFuture = executor.submit(() ->
                    studentRepository.findById(studentId)
                            .orElseThrow(() -> new EntityNotFoundException("Student not found"))
            );

            Future<Mentor> mentorFuture = executor.submit(() ->
                    mentorRepository.findById(request.getMentorId())
                            .orElseThrow(() -> new EntityNotFoundException("Mentor not found"))
            );

            Student student = studentFuture.get();
            Mentor mentor = mentorFuture.get();

            Session session = new Session(student, mentor, SessionStatus.PENDING, request.getDate());
            sessionRepository.save(session);

        }
        catch (InterruptedException | ExecutionException e) {
            throw new EntityNotFoundException(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}
