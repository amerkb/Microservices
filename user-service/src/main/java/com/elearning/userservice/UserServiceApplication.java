package com.elearning.userservice;

import com.elearning.userservice.model.User;
import com.elearning.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner createAdmin(UserRepository userRepository) {
		return args -> {
			Optional<User> adminOpt = userRepository.findByEmail("admin@elearning.com");
			if (adminOpt.isEmpty()) {
				User admin = new User();
				admin.setEmail("admin@elearning.com");
				admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
				admin.setRole("ADMIN");
				userRepository.save(admin);
				System.out.println("✅ Admin user created: admin@elearning.com / admin123");
			} else {
				System.out.println("ℹ️ Admin user already exists.");
			}
		};
	}
}
