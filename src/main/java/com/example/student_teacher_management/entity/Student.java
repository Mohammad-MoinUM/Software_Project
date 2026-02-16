package com.example.student_teacher_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String rollNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String course;

    private String phoneNumber;

    private String address;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
