package com.example.student_teacher_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String name;
    private String rollNumber;
    private String email;
    private String course;
    private String phoneNumber;
    private String address;
    private String username;
    private String password;
}
