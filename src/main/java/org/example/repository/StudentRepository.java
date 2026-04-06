package org.example.repository;

import org.example.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * JpaRepository<Student, Long>:
 * - Student = the entity type
 * - Long    = the type of the primary key (id)
 *
 * Inherited methods (no code needed):
 * - save(student)           -> INSERT or UPDATE
 * - findById(id)            -> SELECT WHERE id = ?
 * - findAll()               -> SELECT all
 * - deleteById(id)          -> DELETE WHERE id = ?
 * - existsById(id)          -> SELECT COUNT(*)
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Custom query method - Spring Data generates SQL automatically
    // SELECT * FROM students WHERE email = ?
    Optional<Student> findByEmail(String email);
}

