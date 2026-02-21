package com.university.erp.controller;

import com.university.erp.entity.Teacher;
import com.university.erp.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public String listTeachers(Model model) {
        List<Teacher> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        return "teachers/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "teachers/form";
    }

    @PostMapping
    public String createTeacher(@ModelAttribute Teacher teacher) {
        teacherService.createTeacher(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Teacher teacher = teacherService.getTeacherById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        model.addAttribute("teacher", teacher);
        return "teachers/form";
    }

    @PostMapping("/update")
    public String updateTeacher(@ModelAttribute Teacher teacher) {
        teacherService.updateTeacher(teacher.getId(), teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return "redirect:/teachers";
    }
}