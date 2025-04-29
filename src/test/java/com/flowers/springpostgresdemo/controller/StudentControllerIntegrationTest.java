package com.flowers.springpostgresdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowers.springpostgresdemo.model.Grade;
import com.flowers.springpostgresdemo.model.Student;
import com.flowers.springpostgresdemo.repository.GradeRepository;
import com.flowers.springpostgresdemo.repository.StudentRepository;
import com.flowers.springpostgresdemo.service.GradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class StudentControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("students_db_test")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("init.sql");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private GradeService gradeService;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        gradeRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void testCreateStudent() throws Exception {
        // Create a student object
        Student student = new Student();
        student.setFirstName("Test");
        student.setLastName("Student");
        student.setEmail("test.student@example.com");
        student.setAge(25);

        // Convert student object to JSON
        String studentJson = objectMapper.writeValueAsString(student);

        // Send POST request to create student
        MvcResult result = mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract created student from response
        String responseContent = result.getResponse().getContentAsString();
        Student createdStudent = objectMapper.readValue(responseContent, Student.class);

        // Verify the response
        assertNotNull(createdStudent.getId());
        assertEquals("Test", createdStudent.getFirstName());
        assertEquals("Student", createdStudent.getLastName());
        assertEquals("test.student@example.com", createdStudent.getEmail());
        assertEquals(25, createdStudent.getAge());

        // Verify that the student was saved in the repository
        Optional<Student> savedStudent = studentRepository.findByEmail("test.student@example.com");
        assertTrue(savedStudent.isPresent());
        assertEquals("Test", savedStudent.get().getFirstName());
        assertEquals("Student", savedStudent.get().getLastName());
        assertEquals(25, savedStudent.get().getAge());

        Long studentId = savedStudent.get().getId();
        assertNotNull(studentId);

        // Create a grade for the student
        Grade grade = new Grade();
        grade.setSubjectName("Test Subject");
        grade.setGrade(95.0);
        grade.setStudent(savedStudent.get());

        // Save the grade
        Grade savedGrade = gradeService.createGrade(grade);

        // Verify that the grade was saved in the repository
        assertNotNull(savedGrade.getId());
        assertEquals("Test Subject", savedGrade.getSubjectName());
        assertEquals(95.0, savedGrade.getGrade());
        assertEquals(studentId, savedGrade.getStudent().getId());

        // Verify that the grade is associated with the student
        List<Grade> studentGrades = gradeRepository.findByStudentId(studentId);
        assertEquals(1, studentGrades.size());
        assertEquals("Test Subject", studentGrades.get(0).getSubjectName());
        assertEquals(95.0, studentGrades.get(0).getGrade());

        // Print debug information
        System.out.println("[DEBUG_LOG] Student created with ID: " + studentId);
        System.out.println("[DEBUG_LOG] Grade created with ID: " + savedGrade.getId());
        System.out.println("[DEBUG_LOG] Student repository count: " + studentRepository.count());
        System.out.println("[DEBUG_LOG] Grade repository count: " + gradeRepository.count());
    }
}
