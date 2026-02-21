package com.university.erp.controller;

import com.university.erp.entity.Student;
import com.university.erp.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // 1. List all students (GET /students)
    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "students/list"; // Looks for src/main/resources/templates/students/list.html
    }

    // 2. Show Create Form (GET /students/new)
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/form"; // Looks for src/main/resources/templates/students/form.html
    }

    // 3. Create Student (POST /students)
    @PostMapping
    public String createStudent(@ModelAttribute Student student) {
        studentService.createStudent(student);
        return "redirect:/students"; // Redirect to list after save (Prevents form resubmission)
    }

    // 4. Show Update Form (GET /students/edit/{id})
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        model.addAttribute("student", student);
        return "students/form"; // Reuse the same form for create and update
    }

    // 5. Update Student (POST /students/update)
    @PostMapping("/update")
    public String updateStudent(@ModelAttribute Student student) {
        studentService.updateStudent(student.getId(), student);
        return "redirect:/students";
    }

    // 6. Delete Student (GET /students/delete/{id}) I will use DELETE later
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Student currentStudent = studentService.findByEmail(email);

        model.addAttribute("student", currentStudent);
        return "students/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute Student studentDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Student currentStudent = studentService.findByEmail(email);

        currentStudent.setName(studentDetails.getName());
        studentService.updateStudent(currentStudent.getId(), currentStudent);

        return "redirect:/students/profile?success";
    }
}