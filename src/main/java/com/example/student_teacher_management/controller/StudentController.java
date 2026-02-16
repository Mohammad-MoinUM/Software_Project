package com.example.student_teacher_management.controller;

import com.example.student_teacher_management.dto.StudentDTO;
import com.example.student_teacher_management.entity.Student;
import com.example.student_teacher_management.entity.User;
import com.example.student_teacher_management.repository.UserRepository;
import com.example.student_teacher_management.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Student student = studentService.getStudentByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Student profile not found"));

            model.addAttribute("student", student);
            return "student/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/profile/edit")
    public String showEditForm(Authentication authentication, Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Student student = studentService.getStudentByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Student profile not found"));

            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setId(student.getId());
            studentDTO.setName(student.getName());
            studentDTO.setRollNumber(student.getRollNumber());
            studentDTO.setEmail(student.getEmail());
            studentDTO.setCourse(student.getCourse());
            studentDTO.setPhoneNumber(student.getPhoneNumber());
            studentDTO.setAddress(student.getAddress());

            model.addAttribute("studentDTO", studentDTO);
            return "student/profile-edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/student/profile";
        }
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication authentication,
                               @ModelAttribute StudentDTO studentDTO,
                               RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Student student = studentService.getStudentByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Student profile not found"));

            // Students cannot update roll number
            studentService.updateStudent(student.getId(), studentDTO, false);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
            return "redirect:/student/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/student/profile/edit";
        }
    }
}
