package com.university.erp.integration;

import com.university.erp.config.SecurityConfig;
import com.university.erp.controller.CourseController;
import com.university.erp.entity.Course;
import com.university.erp.security.CustomUserDetailsService;
import com.university.erp.service.CourseService;
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

@WebMvcTest(CourseController.class)
@Import(SecurityConfig.class)
class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setName("Introduction to Java");
        testCourse.setCode("CSE101");
        testCourse.setCredit(3.0f);
    }

    // ---- LIST ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testListCourses_AsTeacher() throws Exception {
        when(courseService.getAllCourses()).thenReturn(Arrays.asList(testCourse));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/list"))
                .andExpect(model().attributeExists("courses"));

        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testListCourses_AsStudent() throws Exception {
        when(courseService.getAllCourses()).thenReturn(Arrays.asList(testCourse));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/list"));
    }

    @Test
    void testListCourses_Unauthenticated() throws Exception {
        mockMvc.perform(get("/courses"))
                .andExpect(status().is3xxRedirection()); // redirects to login
    }

    // ---- SHOW CREATE FORM ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testShowCreateForm_AsTeacher() throws Exception {
        mockMvc.perform(get("/courses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/form"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testShowCreateForm_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/courses/new"))
                .andExpect(status().isForbidden());
    }

    // ---- CREATE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testCreateCourse_AsTeacher() throws Exception {
        when(courseService.createCourse(any(Course.class))).thenReturn(testCourse);

        mockMvc.perform(post("/courses")
                        .with(csrf())
                        .param("name", "Introduction to Java")
                        .param("code", "CSE101")
                        .param("credit", "3.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(courseService, times(1)).createCourse(any(Course.class));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testCreateCourse_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(post("/courses")
                        .with(csrf())
                        .param("name", "Introduction to Java")
                        .param("code", "CSE101")
                        .param("credit", "3.0"))
                .andExpect(status().isForbidden());
    }

    // ---- SHOW EDIT FORM ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testShowEditForm_AsTeacher() throws Exception {
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(testCourse));

        mockMvc.perform(get("/courses/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/form"))
                .andExpect(model().attributeExists("course"));

        verify(courseService, times(1)).getCourseById(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testShowEditForm_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/courses/edit/1"))
                .andExpect(status().isForbidden());
    }

    // ---- UPDATE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testUpdateCourse_AsTeacher() throws Exception {
        when(courseService.updateCourse(eq(1L), any(Course.class))).thenReturn(testCourse);

        mockMvc.perform(post("/courses/update")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "Advanced Java")
                        .param("code", "CSE201")
                        .param("credit", "4.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(courseService, times(1)).updateCourse(eq(1L), any(Course.class));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testUpdateCourse_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(post("/courses/update")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "Advanced Java")
                        .param("code", "CSE201")
                        .param("credit", "4.0"))
                .andExpect(status().isForbidden());
    }

    // ---- DELETE ----

    @Test
    @WithMockUser(roles = "TEACHER")
    void testDeleteCourse_AsTeacher() throws Exception {
        doNothing().when(courseService).deleteCourse(1L);

        mockMvc.perform(get("/courses/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(courseService, times(1)).deleteCourse(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testDeleteCourse_AsStudent_Forbidden() throws Exception {
        mockMvc.perform(get("/courses/delete/1"))
                .andExpect(status().isForbidden());
    }
}
