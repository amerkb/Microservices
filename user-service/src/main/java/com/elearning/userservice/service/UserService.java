package com.elearning.userservice.service;

import com.elearning.userservice.model.User;
import com.elearning.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private  UserRepository repo;
    @Autowired
    private  JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public String login(String email, String password) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // ✅ Generate JWT and return it
        return jwtService.generateToken(user.getEmail(), user.getRole());
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public User findById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
