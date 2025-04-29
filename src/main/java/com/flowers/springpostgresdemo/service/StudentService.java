package com.flowers.springpostgresdemo.service;

import com.flowers.springpostgresdemo.model.Student;
import com.flowers.springpostgresdemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Get student by email
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    // Get students by last name
    public List<Student> getStudentsByLastName(String lastName) {
        return studentRepository.findByLastName(lastName);
    }

    // Get students by minimum age
    public List<Student> getStudentsByMinimumAge(Integer age) {
        return studentRepository.findByAgeGreaterThanEqual(age);
    }

    // Create a new student
    @Transactional
    public Student createStudent(Student student) {
        // Check if email already exists
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    // Update an existing student
    @Transactional
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        // Check if email is being changed and if it already exists
        if (!student.getEmail().equals(studentDetails.getEmail()) &&
                studentRepository.findByEmail(studentDetails.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + studentDetails.getEmail());
        }

        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setEmail(studentDetails.getEmail());
        student.setAge(studentDetails.getAge());

        return studentRepository.save(student);
    }

    // Delete a student
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}
