package com.university.erp.controller;

import com.university.erp.entity.Department;
import com.university.erp.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listDepartments(Model model) {
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "departments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/form";
    }

    @PostMapping
    public String createDepartment(@ModelAttribute Department department) {
        departmentService.createDepartment(department);
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        model.addAttribute("department", department);
        return "departments/form";
    }

    @PostMapping("/update")
    public String updateDepartment(@ModelAttribute Department department) {
        departmentService.updateDepartment(department.getId(), department);
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }
}