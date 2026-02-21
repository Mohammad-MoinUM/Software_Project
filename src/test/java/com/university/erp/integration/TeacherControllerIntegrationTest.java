package com.university.erp.integration;

import com.university.erp.config.SecurityConfig;
import com.university.erp.controller.TeacherController;
import com.university.erp.entity.Teacher;
import com.university.erp.security.CustomUserDetailsService;
import com.university.erp.service.TeacherService;
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

@WebMvcTest(TeacherController.class)
@Import(SecurityConfig.class)
class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeacherService teacherService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setName("Dr. Smith");
        testTeacher.setEmployeeId(5001);
        testTeacher.setEmail("smith@university.edu");
        testTeacher.setPassword("encryptedPassword123");
        testTeacher.setRole("TEACHER");
    }

    // ---- LIST ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testListTeachers_AsTeacher() throws Exception {
        when(teacherService.getAllTeachers()).thenReturn(Arrays.asList(testTeacher));

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/list"))
                .andExpect(model().attributeExists("teachers"));

        verify(teacherService, times(1)).getAllTeachers();
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testListTeachers_AsStudent() throws Exception {
        when(teacherService.getAllTeachers()).thenReturn(Arrays.asList(testTeacher));

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/list"));
    }

    @Test
    void testListTeachers_Unauthenticated() throws Exception {
        mockMvc.perform(get("/teachers"))
                .andExpect(status().is3xxRedirection());
    }

    // ---- SHOW CREATE FORM ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testShowCreateForm_AsTeacher() throws Exception {
        mockMvc.perform(get("/teachers/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/form"))
                .andExpect(model().attributeExists("teacher"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testShowCreateForm_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/teachers/new"))
                .andExpect(status().isForbidden());
    }

    // ---- CREATE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testCreateTeacher_AsTeacher() throws Exception {
        when(teacherService.createTeacher(any(Teacher.class))).thenReturn(testTeacher);

        mockMvc.perform(post("/teachers")
                        .with(csrf())
                        .param("name", "Dr. Smith")
                        .param("employeeId", "5001")
                        .param("email", "smith@university.edu")
                        .param("password", "encryptedPassword123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService, times(1)).createTeacher(any(Teacher.class));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testCreateTeacher_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(post("/teachers")
                        .with(csrf())
                        .param("name", "Dr. Smith")
                        .param("employeeId", "5001")
                        .param("email", "smith@university.edu")
                        .param("password", "encryptedPassword123"))
                .andExpect(status().isForbidden());
    }

    // ---- SHOW EDIT FORM ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testShowEditForm_AsTeacher() throws Exception {
        when(teacherService.getTeacherById(1L)).thenReturn(Optional.of(testTeacher));

        mockMvc.perform(get("/teachers/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/form"))
                .andExpect(model().attributeExists("teacher"));

        verify(teacherService, times(1)).getTeacherById(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testShowEditForm_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/teachers/edit/1"))
                .andExpect(status().isForbidden());
    }

    // ---- UPDATE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testUpdateTeacher_AsTeacher() throws Exception {
        when(teacherService.updateTeacher(eq(1L), any(Teacher.class))).thenReturn(testTeacher);

        mockMvc.perform(post("/teachers/update")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "Dr. Smith Jr.")
                        .param("employeeId", "5010")
                        .param("email", "smithjr@university.edu")
                        .param("password", "newPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService, times(1)).updateTeacher(eq(1L), any(Teacher.class));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testUpdateTeacher_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(post("/teachers/update")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "Dr. Smith Jr.")
                        .param("employeeId", "5010")
                        .param("email", "smithjr@university.edu")
                        .param("password", "newPassword"))
                .andExpect(status().isForbidden());
    }

    // ---- DELETE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testDeleteTeacher_AsTeacher() throws Exception {
        doNothing().when(teacherService).deleteTeacher(1L);

        mockMvc.perform(get("/teachers/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService, times(1)).deleteTeacher(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testDeleteTeacher_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/teachers/delete/1"))
                .andExpect(status().isForbidden());
    }
}
