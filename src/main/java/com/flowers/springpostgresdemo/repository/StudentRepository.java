package com.flowers.springpostgresdemo.repository;

import com.flowers.springpostgresdemo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Find student by email
    Optional<Student> findByEmail(String email);
    
    // Find students by last name
    List<Student> findByLastName(String lastName);
    
    // Find students by age greater than or equal to
    List<Student> findByAgeGreaterThanEqual(Integer age);
    
    // Find students by first name and last name
    List<Student> findByFirstNameAndLastName(String firstName, String lastName);

    @Query(value = "SELECT s.* FROM students s " +
                   "JOIN grades g ON s.id = g.student_id " +
                   "WHERE s.first_name LIKE 'A%' AND g.grade > 3",
           nativeQuery = true)
    List<Student> findStudentsWithNameStartingWithAAndGradeGreaterThanThree();
}
