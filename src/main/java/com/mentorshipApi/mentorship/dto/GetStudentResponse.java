package com.mentorshipApi.mentorship.dto;

import com.mentorshipApi.mentorship.entity.Student;

public class GetStudentResponse {
    private Long id;
    private String name;
    private String email;

    public static GetStudentResponse fromEntity(Student student) {
        return new GetStudentResponse(student.getId(), student.getName(), student.getEmail());
    }

    public GetStudentResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
