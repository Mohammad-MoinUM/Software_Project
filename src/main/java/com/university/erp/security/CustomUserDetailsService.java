package com.university.erp.security;

import com.university.erp.entity.Student;
import com.university.erp.entity.Teacher;
import com.university.erp.repository.StudentRepository;
import com.university.erp.repository.TeacherRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public CustomUserDetailsService(StudentRepository studentRepository,
                                    TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try to find student first
        Student student = studentRepository.findByEmail(email).orElse(null);

        if (student != null) {
            return new User(
                    student.getEmail(),
                    student.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + student.getRole()))
            );
        }

        // Try to find teacher
        Teacher teacher = teacherRepository.findByEmail(email).orElse(null);

        if (teacher != null) {
            return new User(
                    teacher.getEmail(),
                    teacher.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + teacher.getRole()))
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}