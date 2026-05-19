package re.edu;

import java.util.TimeZone;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import re.edu.entity.Users;
import re.edu.repository.UserRepository;
import re.edu.util.enums.Role;

@SpringBootApplication
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    // tạo admin mặc định nếu chưa có
    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {
            boolean exists = userRepository.existsByUsername("admin");
            if (!exists) {
                Users admin = new Users();
                admin.setFullName("Admin");
                admin.setUsername("admin");
                admin.setPasswordHash(passwordEncoder.encode("admin123")); // BCrypt hash
                admin.setEmail("admin@gmail.com");
                admin.setIsActive(true);
                admin.setRole(Role.ADMIN);
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());

                userRepository.save(admin);
            }
        };
    }
}
