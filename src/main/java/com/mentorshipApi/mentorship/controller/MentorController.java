package com.mentorshipApi.mentorship.controller;

import com.mentorshipApi.mentorship.dto.*;
import com.mentorshipApi.mentorship.entity.Session;
import com.mentorshipApi.mentorship.exception.EntityNotFoundException;
import com.mentorshipApi.mentorship.exception.UserAlreadyExistsException;
import com.mentorshipApi.mentorship.service.MentorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mentors")
public class MentorController {
    @Autowired
    MentorService mentorService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<MentorResponse>> getMentorsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        PaginatedResponse<MentorResponse> mentorsPaginated = this.mentorService.getMentorsPaginated(pageable);

        return ResponseEntity.ok(mentorsPaginated);
    }

    @PostMapping
    public ResponseEntity<ApiError> createMentor(@Valid @RequestBody CreateMentorRequest request) {
        try {
            this.mentorService.createMentor(request);
        } catch (UserAlreadyExistsException e) {
            ApiError error = new ApiError();
            error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            error.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{mentorId}/sessions")
    public ResponseEntity<List<SessionResponse>> getSessionsByMentor(
            @PathVariable Long mentorId) {
        final int LIMIT = 5;

        List<Session> sessions = mentorService.getLastSessions(mentorId, LIMIT);
        List<SessionResponse> response = sessions.stream()
                .map(SessionResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{mentorId}/sessions/pending")
    public ResponseEntity<PaginatedResponse<SessionResponse>> getSessionsToApprove(
            @PathVariable Long mentorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").ascending());
        PaginatedResponse<SessionResponse> pendingSessions = mentorService.getSessionsToApprove(mentorId, pageable);

        return ResponseEntity.ok(pendingSessions);
    }

    @PatchMapping("/{mentorId}/sessions/{sessionId}")
    public ResponseEntity approveOrRejectSession(
            @Valid @RequestBody ChangeSessionStatus request,
            @PathVariable Long mentorId,
            @PathVariable Long sessionId
    ) {
        try {
            this.mentorService.changeSessionStatus(sessionId, request.getNewStatus());
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            ApiError error = new ApiError();
            error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            error.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
        }

        return ResponseEntity.noContent().build();
    }
}
