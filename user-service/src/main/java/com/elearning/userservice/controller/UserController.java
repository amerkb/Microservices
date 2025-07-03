package com.elearning.userservice.controller;

import com.elearning.userservice.model.User;
import com.elearning.userservice.service.JwtService;
import com.elearning.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired

    private  UserService service;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setRole("USER");
        return ResponseEntity.ok(service.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        String token = service.login(email, password); // ✅ return JWT
        return ResponseEntity.ok(Map.of("token", token));
    }
    @PostMapping("/trainers")
    public ResponseEntity<?> addTrainer(@RequestBody User user, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String role = jwtService.extractRole(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        user.setRole("TRAINER");
        User saved = service.register(user);
        return ResponseEntity.ok(saved);
    }


    @GetMapping
    public List<?> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Object byId(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        // استخراج بيانات المستخدم من التوكن
        var jwtUser = jwtService.extractUser(token);

        // البحث عن المستخدم من قاعدة البيانات باستخدام ID
        User user = service.findById(jwtUser.getId());

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        return ResponseEntity.ok(user);
    }
}
