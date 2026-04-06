package org.example.controller;

import org.example.entity.Student;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @RestController  = @Controller + @ResponseBody (returns JSON by default)
 * @RequestMapping  = base URL prefix for all endpoints in this class
 * @CrossOrigin     = allows requests from browser frontends (CORS)
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // -----------------------------------------------
    // POST /api/students  -> Create a new student
    // -----------------------------------------------
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student saved = studentService.createStudent(student);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);  // 201 Created
    }

    // -----------------------------------------------
    // GET /api/students  -> Get all students
    // -----------------------------------------------
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());  // 200 OK
    }

    // -----------------------------------------------
    // GET /api/students/{id}  -> Get student by ID (CACHED)
    // First call  = Cache MISS (queries PostgreSQL)
    // Second call = Cache HIT  (returns from Redis)
    // -----------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(student -> {
                    System.out.println(">>> Returning student: " + student.getName());
                    return ResponseEntity.ok(student);
                })
                .orElse(ResponseEntity.notFound().build());  // 404 if not found
    }

    // -----------------------------------------------
    // PUT /api/students/{id}  -> Update student
    // Also updates the Redis cache via @CachePut
    // -----------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {
        try {
            Student updated = studentService.updateStudent(id, student);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // -----------------------------------------------
    // DELETE /api/students/{id}  -> Delete student
    // Also removes entry from Redis cache via @CacheEvict
    // -----------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
