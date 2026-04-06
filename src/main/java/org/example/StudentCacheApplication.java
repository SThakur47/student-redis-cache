package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @SpringBootApplication combines:
 *   - @Configuration       (Spring config class)
 *   - @EnableAutoConfiguration (auto-configures beans)
 *   - @ComponentScan       (scans for @Component, @Service, etc.)
 *
 * @EnableCaching - CRITICAL: Activates Spring Cache annotations.
 *   Without this, @Cacheable, @CacheEvict, @CachePut do NOTHING.
 */
@SpringBootApplication
@EnableCaching
public class StudentCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentCacheApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  Student Cache API is running!");
        System.out.println("  URL: http://localhost:8080/api/students");
        System.out.println("========================================\n");
    }
}

