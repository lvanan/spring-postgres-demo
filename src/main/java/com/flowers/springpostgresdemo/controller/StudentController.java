package com.flowers.springpostgresdemo.controller;

import com.flowers.springpostgresdemo.model.Grade;
import com.flowers.springpostgresdemo.model.Student;
import com.flowers.springpostgresdemo.service.GradeService;
import com.flowers.springpostgresdemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final GradeService gradeService;

    @Autowired
    public StudentController(StudentService studentService, GradeService gradeService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(student -> new ResponseEntity<>(student, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get student with grades by ID
    @GetMapping("/{id}/with-grades")
    public ResponseEntity<?> getStudentWithGrades(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(student -> {
                    // Fetch grades for this student
                    List<Grade> grades = gradeService.getGradesByStudentId(id);

                    // Create a response map with student and grades
                    Map<String, Object> response = new HashMap<>();
                    response.put("student", student);
                    response.put("grades", grades);

                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(Map.of("error", "Student not found with id: " + id), HttpStatus.NOT_FOUND));
    }

    // Get students by last name
    @GetMapping("/by-lastname/{lastName}")
    public ResponseEntity<List<Student>> getStudentsByLastName(@PathVariable String lastName) {
        List<Student> students = studentService.getStudentsByLastName(lastName);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Get students by minimum age
    @GetMapping("/by-age/{age}")
    public ResponseEntity<List<Student>> getStudentsByMinimumAge(@PathVariable Integer age) {
        List<Student> students = studentService.getStudentsByMinimumAge(age);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Create a new student
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            Student createdStudent = studentService.createStudent(student);
            return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Update an existing student
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        try {
            Student updatedStudent = studentService.updateStudent(id, studentDetails);
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return new ResponseEntity<>("Student deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
