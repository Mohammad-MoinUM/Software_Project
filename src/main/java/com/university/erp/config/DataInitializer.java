package com.university.erp.config;

import com.university.erp.entity.Student;
import com.university.erp.entity.Teacher;
import com.university.erp.repository.StudentRepository;
import com.university.erp.repository.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(StudentRepository studentRepository,
                                   TeacherRepository teacherRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {

            // Create test student only if not exists
            if (!studentRepository.existsByEmail("student@university.edu")) {
                Student student = new Student();
                student.setName("TestStudent");
                student.setRoll(1001);
                student.setEmail("student@university.edu");
                student.setPassword(passwordEncoder.encode("password123"));
                student.setRole("STUDENT");
                studentRepository.save(student);

                System.out.println("Test student created!");
            }

            // Create test teacher only if not exists
            if (!teacherRepository.existsByEmail("teacher@university.edu")) {
                Teacher teacher = new Teacher();
                teacher.setName("TestTeacher");
                teacher.setEmployeeId(5001);
                teacher.setEmail("teacher@university.edu");
                teacher.setPassword(passwordEncoder.encode("password123"));
                teacher.setRole("TEACHER");
                teacherRepository.save(teacher);

                System.out.println("Test teacher created!");
            }

            System.out.println("Default users ready!");
        };
    }
}