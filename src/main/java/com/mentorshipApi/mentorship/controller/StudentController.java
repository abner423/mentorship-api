package com.mentorshipApi.mentorship.controller;

import com.mentorshipApi.mentorship.dto.*;
import com.mentorshipApi.mentorship.entity.Session;
import com.mentorshipApi.mentorship.exception.UserAlreadyExistsException;
import com.mentorshipApi.mentorship.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentService service;

    @GetMapping
    public ResponseEntity<PaginatedResponse<GetStudentResponse>> getStudents(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        PaginatedResponse<GetStudentResponse> response = this.service.getStudentsPaginated(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity createStudent(@Valid @RequestBody CreateStudentRequest request) {
        try {
            this.service.createStudent(request);
        } catch (UserAlreadyExistsException e) {
            ApiError error = new ApiError();
            error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            error.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{studentId}/sessions")
    public ResponseEntity<List<SessionResponse>> getSessionsByStudent(
            @PathVariable Long studentId) {
        final int LIMIT = 5;

        List<Session> sessions = service.getLastSessions(studentId, LIMIT);
        List<SessionResponse> response = sessions.stream()
                .map(SessionResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studentId}/sessions/next")
    public ResponseEntity<NextSessionResponse> getNextSessionOfStudent(
            @PathVariable Long studentId) {
        NextSessionResponse nextSession = service.getNextSession(studentId);
        return ResponseEntity.ok(nextSession);
    }

    @PostMapping("/{studentId}/sessions")
    public ResponseEntity requestSession(
            @Valid @RequestBody RequestSessionRequest request,
            @PathVariable Long studentId
    ) {
        try {
            service.requestSessionToMentor(studentId, request);
        } catch (EntityNotFoundException e) {
            ApiError error = new ApiError();
            error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            error.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
