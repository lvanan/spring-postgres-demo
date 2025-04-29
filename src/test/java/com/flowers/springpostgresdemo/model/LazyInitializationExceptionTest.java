package com.flowers.springpostgresdemo.model;

import com.flowers.springpostgresdemo.repository.GradeRepository;
import com.flowers.springpostgresdemo.repository.StudentRepository;
import com.flowers.springpostgresdemo.service.GradeService;
import com.flowers.springpostgresdemo.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class LazyInitializationExceptionTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("students_db_test")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("init.sql");

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @AfterEach
    void tearDown() {
        gradeRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void testLazyInitializationException() {
        Student student = transactionTemplate.execute(status -> {
            Student newStudent = new Student();
            newStudent.setFirstName("Lazy");
            newStudent.setLastName("Student");
            newStudent.setEmail("lazy.student@example.com");
            newStudent.setAge(20);
            Student savedStudent = studentRepository.save(newStudent);

            Grade mathGrade = new Grade();
            mathGrade.setSubjectName("Math");
            mathGrade.setGrade(90.0);
            mathGrade.setStudent(savedStudent);
            gradeRepository.save(mathGrade);

            Grade scienceGrade = new Grade();
            scienceGrade.setSubjectName("Science");
            scienceGrade.setGrade(85.0);
            scienceGrade.setStudent(savedStudent);
            gradeRepository.save(scienceGrade);

            return savedStudent;
        });

        assertNotNull(student);
        assertNotNull(student.getId());

        Student retrievedStudent = studentRepository.findById(student.getId()).orElse(null);
        assertNotNull(retrievedStudent);

        try {
            List<Grade> grades = retrievedStudent.getGrades();

            fail("Expected LazyInitializationException was not thrown");
        } catch (Exception e) {
            assertTrue(e.getClass().getName().contains("LazyInitialization") ||
                       e.getMessage().contains("could not initialize proxy") ||
                       e.getMessage().contains("no Session"),
                      "Expected LazyInitializationException but got: " + e.getClass().getName());
        }
    }
}
