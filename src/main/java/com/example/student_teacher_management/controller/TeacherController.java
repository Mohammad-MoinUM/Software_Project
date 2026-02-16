package com.example.student_teacher_management.controller;

import com.example.student_teacher_management.dto.StudentDTO;
import com.example.student_teacher_management.entity.Student;
import com.example.student_teacher_management.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/teacher")
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "teacher/students-list";
    }

    @GetMapping("/students/new")
    public String showCreateForm(Model model) {
        model.addAttribute("studentDTO", new StudentDTO());
        return "teacher/student-form";
    }

    @PostMapping("/students")
    public String createStudent(@ModelAttribute StudentDTO studentDTO,
                                RedirectAttributes redirectAttributes) {
        try {
            studentService.createStudent(studentDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Student created successfully!");
            return "redirect:/teacher/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teacher/students/new";
        }
    }

    @GetMapping("/students/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Student student = studentService.getStudentById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setId(student.getId());
            studentDTO.setName(student.getName());
            studentDTO.setRollNumber(student.getRollNumber());
            studentDTO.setEmail(student.getEmail());
            studentDTO.setCourse(student.getCourse());
            studentDTO.setPhoneNumber(student.getPhoneNumber());
            studentDTO.setAddress(student.getAddress());

            model.addAttribute("studentDTO", studentDTO);
            return "teacher/student-edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teacher/students";
        }
    }

    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id,
                                @ModelAttribute StudentDTO studentDTO,
                                RedirectAttributes redirectAttributes) {
        try {
            studentService.updateStudent(id, studentDTO, true);
            redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
            return "redirect:/teacher/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teacher/students/" + id + "/edit";
        }
    }

    @GetMapping("/students/{id}/view")
    public String viewStudent(@PathVariable Long id, Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            Student student = studentService.getStudentById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            model.addAttribute("student", student);
            return "teacher/student-view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/teacher/students";
        }
    }

    @PostMapping("/students/{id}/delete")
    public String deleteStudent(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/teacher/students";
    }
}
