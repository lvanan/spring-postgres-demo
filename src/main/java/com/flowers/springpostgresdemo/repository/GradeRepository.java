package com.flowers.springpostgresdemo.repository;

import com.flowers.springpostgresdemo.model.Grade;
import com.flowers.springpostgresdemo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    // Find grades by student
    List<Grade> findByStudent(Student student);
    
    // Find grades by student ID
    List<Grade> findByStudentId(Long studentId);
    
    // Find grades by subject name
    List<Grade> findBySubjectName(String subjectName);
    
    // Find grades by subject name and student
    List<Grade> findBySubjectNameAndStudent(String subjectName, Student student);
}
