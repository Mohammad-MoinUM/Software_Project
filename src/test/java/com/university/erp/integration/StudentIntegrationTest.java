package com.university.erp.integration;

import com.university.erp.config.SecurityConfig;
import com.university.erp.controller.StudentController;
import com.university.erp.entity.Student;
import com.university.erp.security.CustomUserDetailsService;
import com.university.erp.service.StudentService;
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

@WebMvcTest(StudentController.class)
@Import(SecurityConfig.class)
class StudentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("Test Student");
        testStudent.setRoll(1001);
        testStudent.setEmail("test@university.edu");
        testStudent.setPassword("encryptedPassword123");
        testStudent.setRole("STUDENT");
    }

    // ---- LIST ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testListStudents_AsTeacher() throws Exception {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(testStudent));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/list"))
                .andExpect(model().attributeExists("students"));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testListStudents_AsStudent() throws Exception {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(testStudent));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/list"));
    }

    @Test
    void testListStudents_Unauthenticated() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().is3xxRedirection());
    }

    // ---- SHOW CREATE FORM ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testShowCreateForm_AsTeacher() throws Exception {
        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/form"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testShowCreateForm_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/students/new"))
                .andExpect(status().isForbidden());
    }

    // ---- CREATE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testCreateStudent_AsTeacher() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(post("/students")
                        .with(csrf())
                        .param("name", "Test Student")
                        .param("roll", "1001")
                        .param("email", "test@university.edu")
                        .param("password", "encryptedPassword123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).createStudent(any(Student.class));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testCreateStudent_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(post("/students")
                        .with(csrf())
                        .param("name", "Test Student")
                        .param("roll", "1001")
                        .param("email", "test@university.edu")
                        .param("password", "encryptedPassword123"))
                .andExpect(status().isForbidden());
    }

    // ---- SHOW EDIT FORM ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testShowEditForm_AsTeacher() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(Optional.of(testStudent));

        mockMvc.perform(get("/students/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/form"))
                .andExpect(model().attributeExists("student"));

        verify(studentService, times(1)).getStudentById(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testShowEditForm_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/students/edit/1"))
                .andExpect(status().isForbidden());
    }

    // ---- UPDATE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testUpdateStudent_AsTeacher() throws Exception {
        when(studentService.updateStudent(eq(1L), any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(post("/students/update")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "Updated Student")
                        .param("roll", "2002")
                        .param("email", "updated@university.edu")
                        .param("password", "newPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).updateStudent(eq(1L), any(Student.class));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testUpdateStudent_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(post("/students/update")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "Updated Student")
                        .param("roll", "2002")
                        .param("email", "updated@university.edu")
                        .param("password", "newPassword"))
                .andExpect(status().isForbidden());
    }

    // ---- DELETE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testDeleteStudent_AsTeacher() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(get("/students/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testDeleteStudent_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/students/delete/1"))
                .andExpect(status().isForbidden());
    }

    // ---- PROFILE ----

    @Test
    @WithMockUser(username = "test@university.edu", roles = "STUDENT")
    void testShowProfile_AsStudent() throws Exception {
        when(studentService.findByEmail("test@university.edu")).thenReturn(testStudent);

        mockMvc.perform(get("/students/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/profile"))
                .andExpect(model().attributeExists("student"));

        verify(studentService, times(1)).findByEmail("test@university.edu");
    }

    @Test
    @WithMockUser(username = "test@university.edu", roles = "STUDENT")
    void testUpdateProfile_AsStudent() throws Exception {
        when(studentService.findByEmail("test@university.edu")).thenReturn(testStudent);
        when(studentService.updateStudent(eq(1L), any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(post("/students/profile/update")
                        .with(csrf())
                        .param("name", "Updated Name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/profile?success"));

        verify(studentService, times(1)).findByEmail("test@university.edu");
        verify(studentService, times(1)).updateStudent(eq(1L), any(Student.class));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testShowProfile_AsTeacher_Forbidden() throws Exception {
        mockMvc.perform(get("/students/profile"))
                .andExpect(status().isForbidden());
    }
}
