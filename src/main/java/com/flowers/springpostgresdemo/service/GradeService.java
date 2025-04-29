package com.flowers.springpostgresdemo.service;

import com.flowers.springpostgresdemo.model.Grade;
import com.flowers.springpostgresdemo.model.Student;
import com.flowers.springpostgresdemo.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    // Get all grades
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    // Get grade by ID
    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }

    // Get grades by student
    public List<Grade> getGradesByStudent(Student student) {
        return gradeRepository.findByStudent(student);
    }

    // Get grades by student ID
    public List<Grade> getGradesByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    // Get grades by subject name
    public List<Grade> getGradesBySubjectName(String subjectName) {
        return gradeRepository.findBySubjectName(subjectName);
    }

    // Create a new grade
    @Transactional
    public Grade createGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    // Update an existing grade
    @Transactional
    public Grade updateGrade(Long id, Grade gradeDetails) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with id: " + id));

        grade.setSubjectName(gradeDetails.getSubjectName());
        grade.setGrade(gradeDetails.getGrade());
        grade.setStudent(gradeDetails.getStudent());

        return gradeRepository.save(grade);
    }

    // Delete a grade
    @Transactional
    public void deleteGrade(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new IllegalArgumentException("Grade not found with id: " + id);
        }
        gradeRepository.deleteById(id);
    }
}
