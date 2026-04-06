package org.example.service;

import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // -----------------------------------------------------------
    // CREATE - No caching needed for creation
    // -----------------------------------------------------------
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    // -----------------------------------------------------------
    // READ ALL - No caching (list changes frequently)
    // -----------------------------------------------------------
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // -----------------------------------------------------------
    // READ BY ID - CACHING APPLIED HERE
    // -----------------------------------------------------------
    /**
     * @Cacheable("students") - Spring Cache behaviour:
     *
     * CACHE MISS (first call):
     *   1. Spring checks Redis: key = "students::1" (for id=1)
     *   2. NOT found -> method body executes -> DB queried
     *   3. Result stored in Redis with key "students::1"
     *   4. Result returned to client
     *
     * CACHE HIT (subsequent calls):
     *   1. Spring checks Redis: key = "students::1"
     *   2. FOUND -> method body SKIPPED (no DB query!)
     *   3. Cached value returned directly
     *
     * key = "#id" means the cache key is the method parameter 'id'
     */
    @Cacheable(value = "students", key = "#id")
    public Optional<Student> getStudentById(Long id) {
        System.out.println(">>> CACHE MISS: Fetching from PostgreSQL for id=" + id);
        return studentRepository.findById(id);
    }

    // -----------------------------------------------------------
    // UPDATE - Must invalidate stale cache
    // -----------------------------------------------------------
    /**
     * @CachePut: Always executes the method AND updates the cache.
     * Use this when you want to update an entity AND keep cache fresh.
     *
     * After update, Redis stores the new Student object under key "students::id"
     */
    @CachePut(value = "students", key = "#id")
    public Student updateStudent(Long id, Student updatedStudent) {
        return studentRepository.findById(id).map(student -> {
            student.setName(updatedStudent.getName());
            student.setEmail(updatedStudent.getEmail());
            student.setCourse(updatedStudent.getCourse());
            System.out.println(">>> Updating student id=" + id + " and refreshing cache");
            return studentRepository.save(student);
        }).orElseThrow(() ->
                new RuntimeException("Student not found with id: " + id)
        );
    }

    // -----------------------------------------------------------
    // DELETE - Must evict (remove) from cache
    // -----------------------------------------------------------
    /**
     * @CacheEvict: Removes the entry from Redis cache.
     * After deletion, the stale cached entry is gone.
     * Next call for this id will go to DB (cache miss -> 404).
     */
    @CacheEvict(value = "students", key = "#id")
    public void deleteStudent(Long id) {
        System.out.println(">>> Deleting student id=" + id + " and evicting from cache");
        studentRepository.deleteById(id);
    }
}
