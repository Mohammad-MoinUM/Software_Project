package com.example.student_teacher_management.config;

import com.example.student_teacher_management.entity.User;
import com.example.student_teacher_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default teacher account if not exists
        if (!userRepository.existsByUsername("teacher")) {
            User teacher = new User();
            teacher.setUsername("teacher");
            teacher.setPassword(passwordEncoder.encode("teacher123"));
            teacher.setRole("TEACHER");
            teacher.setEnabled(true);
            userRepository.save(teacher);
            System.out.println("Default teacher account created - Username: teacher, Password: teacher123");
        }

        // Create a sample student account if not exists (optional - for testing)
        if (!userRepository.existsByUsername("student")) {
            User student = new User();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setRole("STUDENT");
            student.setEnabled(true);
            userRepository.save(student);
            System.out.println("Default student account created - Username: student, Password: student123");
        }
    }
}
