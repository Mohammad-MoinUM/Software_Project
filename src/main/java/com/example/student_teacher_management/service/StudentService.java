package com.example.student_teacher_management.service;

import com.example.student_teacher_management.dto.StudentDTO;
import com.example.student_teacher_management.entity.Student;
import com.example.student_teacher_management.entity.User;
import com.example.student_teacher_management.repository.StudentRepository;
import com.example.student_teacher_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }

    @Transactional
    public Student createStudent(StudentDTO studentDTO) {
        // Check if username already exists
        if (userRepository.existsByUsername(studentDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if roll number already exists
        if (studentRepository.existsByRollNumber(studentDTO.getRollNumber())) {
            throw new RuntimeException("Roll number already exists");
        }

        // Check if email already exists
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create user account
        User user = new User();
        user.setUsername(studentDTO.getUsername());
        user.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        user.setRole("STUDENT");
        user.setEnabled(true);
        user = userRepository.save(user);

        // Create student profile
        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setRollNumber(studentDTO.getRollNumber());
        student.setEmail(studentDTO.getEmail());
        student.setCourse(studentDTO.getCourse());
        student.setPhoneNumber(studentDTO.getPhoneNumber());
        student.setAddress(studentDTO.getAddress());
        student.setUser(user);

        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(Long id, StudentDTO studentDTO, boolean isTeacher) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Update fields
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setCourse(studentDTO.getCourse());
        student.setPhoneNumber(studentDTO.getPhoneNumber());
        student.setAddress(studentDTO.getAddress());

        // Only teacher can update roll number
        if (isTeacher && studentDTO.getRollNumber() != null
                && !studentDTO.getRollNumber().equals(student.getRollNumber())) {
            if (studentRepository.existsByRollNumber(studentDTO.getRollNumber())) {
                throw new RuntimeException("Roll number already exists");
            }
            student.setRollNumber(studentDTO.getRollNumber());
        }

        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        User user = student.getUser();
        studentRepository.delete(student);

        if (user != null) {
            userRepository.delete(user);
        }
    }
}
