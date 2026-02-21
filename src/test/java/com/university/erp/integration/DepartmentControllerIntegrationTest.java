package com.university.erp.integration;

import com.university.erp.config.SecurityConfig;
import com.university.erp.controller.DepartmentController;
import com.university.erp.entity.Department;
import com.university.erp.security.CustomUserDetailsService;
import com.university.erp.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
@Import(SecurityConfig.class)
class DepartmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Computer Science");
    }

    // ---- LIST ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testListDepartments_AsTeacher() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList(testDepartment));

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(view().name("departments/list"))
                .andExpect(model().attributeExists("departments"));

        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testListDepartments_AsStudent() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList(testDepartment));

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(view().name("departments/list"));
    }

    @Test
    void testListDepartments_Unauthenticated() throws Exception {
        mockMvc.perform(get("/departments"))
                .andExpect(status().is3xxRedirection());
    }

    // ---- SHOW CREATE FORM ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testShowCreateForm_AsTeacher() throws Exception {
        mockMvc.perform(get("/departments/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("departments/form"))
                .andExpect(model().attributeExists("department"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testShowCreateForm_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/departments/new"))
                .andExpect(status().isForbidden());
    }

    // ---- CREATE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testCreateDepartment_AsTeacher() throws Exception {
        when(departmentService.createDepartment(any(Department.class))).thenReturn(testDepartment);

        mockMvc.perform(post("/departments")
                        .with(csrf())
                        .param("name", "Computer Science"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments"));

        verify(departmentService, times(1)).createDepartment(any(Department.class));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testCreateDepartment_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(post("/departments")
                        .with(csrf())
                        .param("name", "Computer Science"))
                .andExpect(status().isForbidden());
    }

    // ---- SHOW EDIT FORM ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testShowEditForm_AsTeacher() throws Exception {
        when(departmentService.getDepartmentById(1L)).thenReturn(Optional.of(testDepartment));

        mockMvc.perform(get("/departments/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("departments/form"))
                .andExpect(model().attributeExists("department"));

        verify(departmentService, times(1)).getDepartmentById(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testShowEditForm_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/departments/edit/1"))
                .andExpect(status().isForbidden());
    }

    // ---- UPDATE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testUpdateDepartment_AsTeacher() throws Exception {
        when(departmentService.updateDepartment(eq(1L), any(Department.class))).thenReturn(testDepartment);

        mockMvc.perform(post("/departments/update")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "Electrical Engineering"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments"));

        verify(departmentService, times(1)).updateDepartment(eq(1L), any(Department.class));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testUpdateDepartment_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(post("/departments/update")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "Electrical Engineering"))
                .andExpect(status().isForbidden());
    }

    // ---- DELETE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testDeleteDepartment_AsTeacher() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(get("/departments/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments"));

        verify(departmentService, times(1)).deleteDepartment(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testDeleteDepartment_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/departments/delete/1"))
                .andExpect(status().isForbidden());
    }
}
