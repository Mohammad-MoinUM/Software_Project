package com.university.erp.service;

import com.university.erp.entity.Teacher;
import com.university.erp.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Transactional
    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Transactional
    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));

        teacher.setName(teacherDetails.getName());
        teacher.setEmployeeId(teacherDetails.getEmployeeId());
        teacher.setEmail(teacherDetails.getEmail());

        return teacherRepository.save(teacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
}