package com.flowers.springpostgresdemo.controller;

import com.flowers.springpostgresdemo.model.Grade;
import com.flowers.springpostgresdemo.service.GradeService;
import com.flowers.springpostgresdemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;
    private final StudentService studentService;

    @Autowired
    public GradeController(GradeService gradeService, StudentService studentService) {
        this.gradeService = gradeService;
        this.studentService = studentService;
    }

    // Get all grades
    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        List<Grade> grades = gradeService.getAllGrades();
        return new ResponseEntity<>(grades, HttpStatus.OK);
    }

    // Get grade by ID
    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        return gradeService.getGradeById(id)
                .map(grade -> new ResponseEntity<>(grade, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get grades by student ID
    @GetMapping("/by-student/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudentId(@PathVariable Long studentId) {
        if (!studentService.getStudentById(studentId).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Grade> grades = gradeService.getGradesByStudentId(studentId);
        return new ResponseEntity<>(grades, HttpStatus.OK);
    }

    // Get grades by subject name
    @GetMapping("/by-subject/{subjectName}")
    public ResponseEntity<List<Grade>> getGradesBySubjectName(@PathVariable String subjectName) {
        List<Grade> grades = gradeService.getGradesBySubjectName(subjectName);
        return new ResponseEntity<>(grades, HttpStatus.OK);
    }

    // Create a new grade
    @PostMapping
    public ResponseEntity<?> createGrade(@RequestBody Grade grade) {
        try {
            Grade createdGrade = gradeService.createGrade(grade);
            return new ResponseEntity<>(createdGrade, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Update an existing grade
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrade(@PathVariable Long id, @RequestBody Grade gradeDetails) {
        try {
            Grade updatedGrade = gradeService.updateGrade(id, gradeDetails);
            return new ResponseEntity<>(updatedGrade, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Delete a grade
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrade(@PathVariable Long id) {
        try {
            gradeService.deleteGrade(id);
            return new ResponseEntity<>("Grade deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
